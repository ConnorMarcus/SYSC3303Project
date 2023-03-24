# Group 9 - Iteration 4
Group Members: Vahid Foroughi, Noah Hammoud, Ilyes Hasnaou, Connor Marcus, Patrick Vafaie 

## Project Structure 
The following is a brief description of the Java files contained within the project:
- `Elevator.java` represents the Elevator subsystem which communicates with the Scheduler to process elevator events.
- `ElevatorEvent.java` represents each text line from the input file as an object.
- `ElevatorState.java` represents the state of the elevator subsystem.
- `ElevatorRequest.java` represents a request from the Elevator to the Scheduler indicating it's ready to receive a FloorRequest. 
- `EleavtorResponse.java` represents a response object the elevator sends out to the scheduler after an event has occurred.
- `Floor.java` represents the floor subsystem which handles the parsing of the input file and communicating that with the Scheduler.
- `FloorRequest.java` represents a request made from the floor subsystem to the Scheduler.
- `Scheduler.java` represents the Scheduler subsystem which communicates with both the floor and elevator subsystems.
- `SchedulerState.java` represents the state of the Scheduler subsystem.
- `SchedulerReceivingState.java` represents the concrete state of the scheduler when it is only receiving requests.
- `SchedulerReceivingSendingState.java` represents the concrete state of the scheduler when it can receive and respond to requests
- `Time.java` represents the time stamp from the input file request in the following format: hh:mm:ss.mmm
- `UDPUtil.java` is a utility class for UPD-related functions such as: creating sockets, packets, sending and receiving packets, etc.

*Above classes have associated JUnit test files*

- `floor_input.txt` located within the Resources folder and it contains the input requests read by the Floor subsystem (time, floor, floor button, and car button). You should change this file if you wish to change the requests.

## Responsibilty Breakdown

### Iteration 4
- **Connor**: Added appropriate Iteration 4 changes to Scheduler, Elevator, ElevatorState
- **Ilyes**: UML Class diagram, refactoring
- **Noah**: Added appropriate Iteration 4 changes to Scheduler, Elevator, ElevatorState, ElevatorRequest
- **Patrick**: Timing diagram, refactoring
- **Vahid**: README, added Acknowledgements from Scheduler to Elevator and Floor, JUnit test cases

### Iteration 3
- **Connor**: UDPUtil.java, updated Scheduler, Elevator, and Floor
- **Ilyes**: UML Class diagram, updated Scheduler and Floor
- **Noah**: JUnit test cases, updated Scheduler, Elevator, and Floor
- **Patrick**: Sequence diagram, updated Scheduler, Elevator, and Floor
- **Vahid**: README, updated ElevatorState, ElevatorEvent, Floor, and Time

### Iteration 2
- **Connor**: SchedulerReceivingState.java, SchedulerReceivingSendingState.java, sequence diagram
- **Ilyes**: SchedulerState.java, refactoring, UML class diagram
- **Noah**: ElevatorState.java, state diagram, README
- **Patrick**: JUnit tests, refactoring, README
- **Vahid**: ElevatorState.java, state diagram

### Iteration 1
- **Connor**: Time.java, ElevatorResponse.java, FloorRequest.java and README.txt.
- **Ilyes**: JUnit testing, refactoring and UML Class diagram.
- **Noah**: Elevator.java, ElevatorEvent.java and README.txt
- **Patrick**: Floor.java, refactoring and FloorRequest.java.
- **Vahid**: Scheduler.java, Main.java and UML Sequence diagram.

## Usage 

### Run the project in Eclipse:

1. Load the project into Eclipse using the provided zip inside the submission zip: "LA3G9_milestone_3.zip". To do this click the "File" menu in Eclipse then click “Import”, and under the "General" folder select "Project from Folder or Archive". Now select the previously mentioned zip file from your file system and click “Finish”.

2. Once the project is loaded in Eclipse, it can be run by running the following files in this order: Scheduler.java, Elevator.java, and Floor.java in the "src" directory under the "com.sysc3303.project" package (each file contains a main method).

- It can be run in that order on either 1 or 3 computers. If running on 3 different computers the IP addresses in Scheduler.java and Floor.java must be set (change the ADDRESS constants). To get the IP address of the computer run the following commands:

  - **Windows:** get the Ipv4 address
    ```
    ipconfig /all 
    ```
  - **Mac:**
    ```
    curl ifconfig.me
    ```
- The number of elevators that are running can be changed by changing the NUM_CARS constant in Elevator.java.
- You can adjust the floor_input.txt file to simulate the different kind of faults. 0 corresponds to no fault, 1 corresponds to a transient fault, 2 corresponds to a hard fault, and -1 corresponds to a shutdown event (this should only be used internally to indicate to the Elevator subsystem that the simulation is finished).

3. Once the project has been run, you should see output in the console corresponding to the events sent and received by the Elevator, Scheduler, and Floor subsystems.

### Run the JUnit test cases for the project:

1. Follow the previous steps to set up the project in Eclipse.

2. To run a single test case open the "com.sysc3303.project.test" package in the “src” directory then click on any JUnit Test Class. Now in the Drop down menu select Run > Run As > JUnit Test.

3. Alternatively, all the JUnit Test Classes can be run by right-clicking on the "com.sysc3303.project.test" package and selecting Run As > JUnit Test.

***NOTE:*** If you encounter an error when running the project or the test cases in Eclipse you may need to clean the project by selecting in the menu Project > Clean.
