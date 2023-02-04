# SYSC3303Project Group 9 - Iteration 1

Group Members: Vahid Foroughi, Noah Hammoud, Ilyes Hasnaou, Connor Marcus, Patrick Vafaie 

### Files

The following is a brief description of the files contained within the Eclipse project:
- Elevator.java: represents the Elevator subsystem which communicates with the Scheduler to process elevator events.
- ElevatorEvent.java: represents each text line from the input file as an object.
EleavtorResponse.java: represents a response object the elevator sends out to the scheduler after an event has occurred.
- Floor.java: represents the floor subsystem which handles the parsing of the input file and communicating that with the Scheduler.
- FloorRequest.java: represents a request made from the floor subsystem to the Scheduler.
- Main.java: the main class to execute the program.
- Scheduler.java: represents the Scheduler subsystem which communicates with both the floor and elevator subsystems.
- Time.java: represents the time stamp from the input file request in the following format: hh:mm:ss.mmm
- ElevatorEventTest.java: tests the ElevatorEvent class
- ElevatorResponseTest.java: tests the ElevatorResponse class
- ElevatorTest.java: tests the Elevator class
- FloorRequestTest.java: tests the FloorRequest class
- FloorTest.java: tests the Floor class
- SchedulerTest.java: tests the Scheduler class
- TimeTest.java: tests the Time class
- floor_input.txt: This file is located within the Resources folder of the Eclipse project and it contains the input requests read by the Floor subsystem (time, floor, floor button, and car button). You should change this file if you wish to change the requests.

### Breakdown of Responsibilities

The following is a breakdown of the main responsibilities of each team member:
- Connor: Time.java, ElevatorResponse.java, FloorRequest.java and README.txt.
- Ilyes: JUnit testing, refactoring and UML Class diagram.
- Noah: Elevator.java, ElevatorEvent.java and README.txt
- Patrick: Floor.java, refactoring and FloorRequest.java.
- Vahid: Scheduler.java, Main.java and UML Sequence diagram.

### Set Up and Test Instructions

The following steps should be taken to run the project in Eclipse:
1. Load the project into Eclipse using the provided zip inside the submission zip: "A3G9_iteration_1.zip". To do this click the "File" menu in Eclipse then click “Import”, and under the "General" folder select "Project from Folder or Archive". Now select the previously mentioned zip file from your file system and click “Finish”.
2. Once the project is loaded in Eclipse, it can be run by running Main.java in the "src" directory under the "com.sysc3303.project" package (this file contains the main method).
3. Once the project has been run, you should see output in the console corresponding to the events sent and received by the Elevator and Floor subsystems.

The following steps should be taken to run the JUnit test cases for the project in Eclipse:
1. Follow the previous steps to set up the project in Eclipse.
2. To run a single test case open the "com.sysc3303.project.test" package in the “src” directory then click on any JUnit Test Class. Now in the Drop down menu select Run > Run As > JUnit Test.
2. Alternatively, all the JUnit Test Classes can be run by right clicking on the "com.sysc3303.project.test" package and selecting Run As > JUnit Test.

Note: If you encounter an error when running the project or the test cases in Eclipse you may need to clean the project by selecting in the menu Project > Clean.


