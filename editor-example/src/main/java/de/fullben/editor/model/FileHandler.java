package de.fullben.editor.model;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.stream.Collectors;

public class FileHandler {

  private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
  private Path file;
  private String filename;

  public FileHandler(Path file) {
    this.file = file;
    filename = file.getFileName().toString();
  }

  public FileHandler(String filename) {
    file = null;
    this.filename = filename;
  }

  public boolean hasFile() {
    return file != null;
  }

  public Path getFile() {
    return file;
  }

  public void setFile(Path file) {
    this.file = file;
    if (file == null) {
      return;
    }
    filename = file.getFileName().toString();
  }

  public String getFilename() {
    return filename;
  }

  public String loadFileContents() throws IOException {
    if (isNonExistentFile(file)) {
      return "";
    }
    return Files.lines(file, StandardCharsets.UTF_8)
        .collect(Collectors.joining(System.lineSeparator()));
  }

  public void saveFileContents(String contents) throws IOException {
    if (file == null) {
      throw new NoSuchFileException(null);
    }
    Files.writeString(
        file,
        contents,
        StandardCharsets.UTF_8,
        StandardOpenOption.CREATE,
        StandardOpenOption.TRUNCATE_EXISTING);
  }

  public boolean matchesCurrentFileContents(String contents) throws IOException {
    if (isNonExistentFile(file)) {
      return contents.isBlank();
    }
    return getMd5Checksum(Files.readAllBytes(file))
        .equals(getMd5Checksum(contents.getBytes(StandardCharsets.UTF_8)));
  }

  private String getExternalFileChecksum() throws IOException {
    return getMd5Checksum(Files.readAllBytes(file));
  }

  private String getMd5Checksum(byte[] bytes) {
    MessageDigest md5Digest;
    try {
      md5Digest = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException(e);
    }
    md5Digest.update(bytes);
    return toHexString(md5Digest.digest()).toUpperCase();
  }

  private static String toHexString(byte[] bytes) {
    char[] hexChars = new char[bytes.length * 2];
    for (int j = 0; j < bytes.length; j++) {
      int v = bytes[j] & 0xFF;
      hexChars[j * 2] = HEX_ARRAY[v >>> 4];
      hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
    }
    return new String(hexChars);
  }

  private static boolean isNonExistentFile(Path file) {
    return file == null || !Files.exists(file) || !Files.isRegularFile(file);
  }
}
