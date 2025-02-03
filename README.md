# Scratch Game

## Overview

The **Scratch Game** is a Java-based application that simulates a simple betting game. The application allows users to make bets and interact with the game via a command-line interface.

This project is built using **Java** and **Maven**, with an **executable JAR** created using **Maven Shade Plugin**.

## Prerequisites

Before running the application, ensure you have the following:

- Java **JDK 8** or later installed.
- Maven installed for building the project.

## Installation

### Clone the Repository

Clone this repository to your local machine using:

```bash
git clone https://github.com/seyid-agha/scratch-game.git


Build the Executable JAR
To build the executable JAR file, navigate to the project folder and run the following Maven command:

mvn clean package

Running the Application
To run the application, use the following command:

java -jar target/scratch-game-1.0-SNAPSHOT.jar --config path-to-config-file --betting-amount some-number

Parameters
--config: Path to the configuration file (in JSON format).
--betting-amount: The amount you want to bet (must be a valid number).
Example:

java -jar target/scratch-game-1.0-SNAPSHOT.jar --config config.json --betting-amount 100

This command will start the game with the specified configuration and betting amount.

