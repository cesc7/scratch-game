# Scratch-game
Probabilistic game where a 3x3 matrix(3x3 is default example in config.json, it can be 4x4,5x5...based on row/column input) is generated from symbols based on predefined probabilities. Users place bets and win or lose based on predefined winning combinations. This project aims to simulate a betting experience with a simple, interactive game.

### Requirements
Java Version: Java 8 or higher  
Build Tool: Maven.   
Dependencies:
Jackson Databind (for JSON parsing)

### Setup Instructions
1. Clone the Repository
   https://github.com/cesc7/scratch-game.git
2. Build the Project    
   mvn clean install


### How to Run

#### Command to Start the Game
Run the game using the generated JAR file:    
java -jar <your-jar-file>.jar --config config.json --betting-amount <amount>

#### Parameters    
--config: Path to the configuration JSON file (e.g., config.json).      
--betting-amount: The amount to bet for the game (e.g., 100).

#### Example Command
java -jar scratch-game-1.0-jar-with-dependencies.jar --config config.json --betting-amount 100        
NOTE: the jar file is in target folder after the build.

