# Group 9 - Iteration 3
Group Members: Vahid Foroughi, Noah Hammoud, Ilyes Hasnaou, Connor Marcus, Patrick Vafaie 

## Files 
The following is a brief description of the Java files contained within the project:
- `Elevator.java` represents the Elevator subsystem which communicates with the Scheduler to process elevator events.
- `ElevatorEvent.java` represents each text line from the input file as an object.
- `ElevatorState.java` represents the state of the elevator subsystem.
- `EleavtorResponse.java` represents a response object the elevator sends out to the scheduler after an event has occurred.
- `Floor.java` represents the floor subsystem which handles the parsing of the input file and communicating that with the Scheduler.
- `FloorRequest.java` represents a request made from the floor subsystem to the Scheduler.
- `Scheduler.java` represents the Scheduler subsystem which communicates with both the floor and elevator subsystems.
- `SchedulerState.java` represents the state of the Scheduler subsystem.
- `SchedulerReceivingState.java` represents the concrete state of the scheduler when it is only receiving requests.
- `SchedulerReceivingSendingState.java` represents the concrete state of the scheduler when it can receive and respond to requests
- `Time.java` represents the time stamp from the input file request in the following format: hh:mm:ss.mmm
- `UDPUtil.java` a utility class for UPD-related functions such as: creating sockets, packets, sending and receiving packets, etc.
- `floor_input.txt` located within the Resources folder and it contains the input requests read by the Floor subsystem (time, floor, floor button, and car button). You should change this file if you wish to change the requests.
- The above classes all have an associated JUnit test file

## Breakdown of Responsibilities 
- **Connor**: 
- **Ilyes**: 
- **Noah**:
- **Patrick**: 
- **Vahid**: 

## Usage 

### Run the project in Eclipse:

1. Load the project into Eclipse using the provided zip inside the submission zip: "LA3G9_milestone_3.zip". To do this click the "File" menu in Eclipse then click “Import”, and under the "General" folder select "Project from Folder or Archive". Now select the previously mentioned zip file from your file system and click “Finish”.

2. Once the project is loaded in Eclipse, it can be run by running the following files in this order: Scheduler.java, Elevator.java, and floor.java in the "src" directory under the "com.sysc3303.project" package (each file contains a main method).

3. Once the project has been run, you should see output in the console corresponding to the events sent and received by the Elevator, Scheduler, and Floor subsystems.

***NOTE:*** if the program is run on three different computers the IP addresses of the computers running Scheduler.java and Floor.java must be set. To get the IP address of the computer run: (Windows) ipconfig /all (get the Ipv4 address) or
(Mac) ifconfig -l (get inet address)

### Run the JUnit test cases for the project:

1. Follow the previous steps to set up the project in Eclipse.

2. To run a single test case open the "com.sysc3303.project.test" package in the “src” directory then click on any JUnit Test Class. Now in the Drop down menu select Run > Run As > JUnit Test.

3. Alternatively, all the JUnit Test Classes can be run by right-clicking on the "com.sysc3303.project.test" package and selecting Run As > JUnit Test.

***NOTE:*** If you encounter an error when running the project or the test cases in Eclipse you may need to clean the project by selecting in the menu Project > Clean.
