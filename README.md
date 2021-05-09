# mvcFX

mvcFX is a library that provides basic support for MVC-based JavaFX projects which want to implement the view and the controller interaction in a less tightly coupled way than intended by the developers of JavaFX. Furthermore, convenience features for changing the application theme and accessing host operating system features are provided.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine.

### Prerequisites

Before downloading the project, you need to make sure that the following software is installed on your machine:

* Java Development Kit 11 (e.g. [AdoptOpenJDK](https://adoptopenjdk.net/))
* [Gradle 6.X](https://gradle.org/releases/)
* [Processing](https://processing.org/download/) (tested with 4.0 alpha 2)

Furthermore, if you want to actively work with the code, consider installing the following software:

* [IntelliJ IDEA](https://www.jetbrains.com/idea/download/) (or an IDE of your choice)
* [Git](https://git-scm.com/downloads)

### Structure

This project contains both the mvcFX library, and an example project. The library can be found in the `mvcfx` subproject, while the text editor example is implemented in the `editor-example` subproject.

### Run the Example

Simply execute the main method of the `de.fullben.editor.Launcher` class.

## Features

The following subsections describe the main features of the mvcFX library.

### MVC Base Classes

The library provides base classes for the views and an interface for implementing the corresponding controllers. Models are not required to implement any library class or interface.

- `View` and `FxmlView`: Views that represent some application component (not necessarily hosted by a dedicated window).
- `StageView` and `FxmlStageView`: Views that represent some application component hosted by a dedicated window.
- `StatelessXXXView`: Views that do not present any mutable state and thus require no model.
- `Controller`: Interface for defining view controllers.

### Alert and File Choosers

Alerts and file choosers can be created via the `Dialogs` class, which provides static factory methods and builders for this purpose.

### Application Theming

The application theme can be changed by providing an implementation of the `Theme` interface to the current view manager by calling `ViewManager.get().setTheme(Theme)`. Note that the theme changes will usually only affect user interface components initialized by implementations of the view base classes or created via any of the methods provided by the `Dialogs` class. 

### Host Access

The library provides implementations for accessing certain host operating system operations (namely opening the file manager and opening a command line interface). The current host OS can be accessed by calling `OperatingSystem.current()`.

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

All changes should honor the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html). Please make sure to update tests and documentation as appropriate.

## License

[MIT](LICENSE.txt)
