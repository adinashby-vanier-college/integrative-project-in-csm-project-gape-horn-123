[![Open in Codespaces](https://classroom.github.com/assets/launch-codespace-2972f46106e565e64193e422d61a12cf1da4916b45550586e14ef0a7c637dd04.svg)](https://classroom.github.com/open-in-codespaces?assignment_repo_id=17781234)
# Integrative Project in Computer Science and Mathematics - Project

This template repository is the starter project for Integrative Project in Computer Science and Mathematics Project. Written in Java, and tested with Gradle/JUnit.

### Project Description

Develop a JavaFX program that implements one or more physical, mathematical and/or chemistry concepts you learned throughout your program.

The program should fetch necessary data from a data source, apply a certain logic to the data and then display the different forms of data on a user interface through necessary animations, forms, and graphs.

The program should have the following characteristics:

**Concepts**

- Implements at least one physical, mathematical or chemistry concept learned in one of the courses. The concept must be of relevance and of an acceptable complexity.

**Interface**

- Displays at least two user interface window using JavaFX.
- Displays either an Animation or a Graph (e.g.: bar graph, scatter plot, ...) that refreshes dynamically based on variable parameters.
- The window must have a menu bar with submenus e.g.: File, Edit, Settings > Preferences

**Parameters**

- Has at least one UI Control for a variable input parameter that affects the Animation or the Graph data display. When the parameter variable changes in the control the display should dynamically change the animation or the displayed data.

**Settings**

- Has a “settings” section where at least four setup parameters are configured. The settings screen should be accessible through the Menu. Settings should be from three categories:
   - General Settings: such as setting the data source (e.g.: Database vs XML), the path of some resources (e.g.: images), or others.
   - Animation or Graph settings: secondary parameters that would not affect the physical concept you implemented such as the size of objects in the animation, or vertical graph vs horizontal graph.
   - Theme settings: settings that affect the layout or style of the interface such as the background color, the font size, or others.
- Retrieves data from a data source, e.g.: Database, XML, JSON, or Web.
- Includes a help button and menu item that displays a window where the physical concept and project descriptions are displayed.

### Logistics

1. The teams should be consisted of ```two to four students max```.
2. All team members must be implicated in the full development i.e., you should understand and be able to implement all parts of the project. This will be a basis of evaluation.
3.	Each team member should develop at least one part that requires integration with the whole project, it could be the implementation of a related concept, a variation of the concept, or an independent concept that can be integrated with the concepts of the other team members as part of the same application.

### Deliverable 1
---
Team members will present their project ideas (PowerPoint) and the selected project description to class.

Each team member must think of and choose a project idea then work with their teammate to select the most convenient/best one. In the proposal PowerPoint, there should be a table containing the information of the following table:

| Project Ideas                            | Description |
| ---------------------------------------- | ----------- |
| Team Member’s name and Project Idea 1:   |             |
| Team Member’s name and Project Idea 2:   |             |
| Team Member’s name and Project Idea 3:   |             |
| Team Member’s name and Project Idea 4:   |             |
| Selected Project Ideas and why:          |             |

The following description should be given in the PowerPoint:

**Concept**
- Describe the physical, mathematical and/or chemistry concept(s) behind the project. 

**Concept Aspects**
- Identify and list the main aspects of the concept such as the problem it addresses, the proposed solution, the solution category among other approaches’ categories. 
- The possible variable parameters that would control the user interface animating the concept.

**Typical Input**
- Describe the typical input for the solution of the applied concept to work.

**Expected Output**
- Describe the expected output and how the user interface would look like and what it would allow the user to do.

**Feasibility**
- List the JavaFX elements and implementation components that you expect to use to implement the project.
- Justify the feasibility in terms of timeline and team tasks assignment.

**Individual part**
- For each team member, describe their individual part and how it would integrate with the whole project with other team members parts.


For this deliverable, you will be evaluated based on the following grid **(5%)**:

- Feasibility - 1%
- Relevance - 1%
- Completeness of the project description - 3%



### Deliverable 2
---
Team members will present their project plan (PowerPoint) to the class.

For this deliverable, the following tasks will be executed:

**Tasks Breakdown, Timeline and Tasks Assignment**

- Create a table and identify the tasks/user stories, to be implemented, assign them to a team member and estimate their durations.
- Create a Jira Kanban board and add all the team members to the board.

**Class Diagram**
- Include a class diagram composed of the key classes that you will be implementing.

**Sample Input and Output Data Grid**
- Include a grid showing sample input and corresponding output data values that will be used to showcase the operating program.

**Wireframes and Mockups**
- Include wireframes of the user interface. 

You could use tools like [Moqups](https://moqups.com/) or [Figma](https://www.figma.com/) for making the mock-up design and wiring.

It is important that the user interface has an area where important data is shown to the users so that they understand what they are looking at. That would include a comprehensive title and updated data values that are reflected in the animation or chart area.

**Sprint Report**
- Include one screenshot of the Trello board in the end of the sprint
- Create a table for the whole team, indicate stories the team starts in this sprint, stories the team resolved.

Create a table for each member, indicate stories she/he received, stories she/he resolved.

For example: 

Sprint 1 - Adin Ashby

| Received Stories          | Resolved Stories         | Carry Over Stories      | Blocked Stories          |
|---------------------------|--------------------------|-------------------------|--------------------------|
| VC-1 Fake story (2)       | VC-1 Fake story (2)      | VC-3 Fake story (3)     |                          |
| VC-3 Fake story (3)       | VC-7 Fake story (1)      |                         |                          |
| VC-7 Fake story (1)       |                          |                         |                          |
| **Total points: 6**       | **Total points: 3**      | **Total points: 3**     | **Total points: 0**      |

For this deliverable, you will be evaluated based on the following grid **(10%)**:

- Correct breakdown of tasks - 2%
- Adequateness of design diagrams - 3%
- Completeness of information provided - 2%
- Wireframes and Mockups - 3%


### Deliverable 3
---

**Part 1**

Deliver the actual implementation of the User Interface with as many graphical elements as possible that represent the entire functionality, the user interface could be a skeleton that does not provide the actual functionalities yet.

For this part, you will be evaluated based on the following grid **(10%)**:

- Appropriate design of User Interface - 7%
- Reasonable consideration of User Experience - 3%

**Part 2**

Deliver the source code that has all the needed assets to run the application. The Part 2 (Beta) version should have as much completed functionality as possible, and it is understandable if it has bugs or broken functionality.

For this part, you will be evaluated based on the following grid **(20%)**:

-	Proper application of Object-Oriented Programming principles - 5%
-  Correct selection and implementation of algorithms - 5%
- 	Correct processing and validation of data inputs - 2%
- 	Correct processing and formatting of data outputs - 2%
-  Correct implementation of the selected concept, or workflow behind the application logic producing adequate results - 6%

**Part 3**

Deliver the production ready final source code that has all the needed assets to package or run the application. The production version is the final version that must be error free, and has the complete functioning features. Also, deliver a deployable package that can be used to run the implemented program, in case Java was used that would consist of a JAR file including all assets that are needed to run the program.

For this part, you will be evaluated based on the following grid **(25%)**:

- Complete implementation of all required features - 5%
- The program executes without errors and all features are usable - 3%
- Appropriate test suite - 3%
- Security of the application e.g. login feature, etc - 2%
- A deployable package is created and correctly executes without additional installation steps - 5%
- Technical documentation and user guide - 2%

In the presentation the following points will be evaluated as well:

- project objectives, concept, or workflow behind the application logic, needs addressed, and developed functionalities - 3%
- Challenges met in the project and solutions - 1%
- Lessons learned - 1%

### PCA Reports
---

```PCA reports should be submitted through Omnivox individually.``` A submission portal will be created under the assignment section on Omnivox.

Submit the following reports in PDF format:

1.	A two- to three-page report written individually by each student assessing his/her performance and contribution to the project and the participation of the other team members.  
2.	A two- to four-page report written individually summarizing:  
   a.	The approach taken for the interdisciplinary project  
   b.	How each discipline of the 200.C1 program contributed to the solution of the project  
   c.	The challenges encountered and the strategies in solving those challenges  
   d.	The strengths, weaknesses as well as the consequences and impact of the project  
3.	A half page, written in French, describing the project.

**Marking Rubric for Overall PCA**

| CRITERIA | VERY COMPETENT | COMPETENT | COMPETENT, WITH SUPPORT | NOT YET COMPETENT |
|----------|----------------|-----------|-------------------------|-------------------|
| **1. Project management** | Develops a well-structured project plan and manages project timelines, completing all project tasks on time. | Develops a structured project plan and usually manages project timelines well, completing most project tasks on time. | Requires some support to develop a project plan and to manage project timelines effectively, completing most project tasks within an acceptable timeframe. | Fails to break down tasks in a project plan and to complete project tasks within an acceptable timeframe. |
| **2. Solution design** | Develops designs using standard notations, according to learned patterns, and that can be used as basis for implementation. | Develops designs with some errors using standard notations, in accordance with learned patterns, and can be used as basis for implementation but following some corrective iterations. | Requires support to develop designs that still contain errors using standard notations, in accordance with learned patterns, and can be used as basis for implementation but following several corrective iterations. | Fails to develop designs using standard notations, according to learned patterns, and designs cannot be used as basis for implementation. |
| **3. Alignment of scientific concepts with programming goals** | Implements an optimized algorithm of optimal complexity based on a scientific concept. | Implements an efficient algorithm of moderate complexity based on a scientific concept. | Implements with some support an efficient algorithm of moderate complexity based on a scientific concept. | Fails to implement an algorithm of moderate complexity based on a scientific concept. |
| **4. Implementation** | Implements well-structured code, complete as planned, and error-free. | Implements well-structured code that needs some adjustments, not complete as planned, or with some errors. | Requires some support to implement structured code, not complete as planned, and with some errors. | Fails to implement structured and complete code; program contains major errors. |
| **5. Testing and troubleshooting skills** | Demonstrates proficient troubleshooting skills by accurately localizing, identifying, fixing, and documenting program bugs and erroneous scientific concepts implementations. | Demonstrates troubleshooting skills by accurately localizing, identifying, fixing, and documenting program bugs and erroneous scientific concepts implementations. | Requires support to troubleshoot programs by accurately localizing, identifying, fixing, and documenting program bugs and erroneous scientific concepts implementations. | Fails to troubleshoot by accurately localizing, identifying, fixing, and documenting program bugs and erroneous scientific concepts implementations. |
| **6. Technical documentation** | Presents highly effective technical documentation, in a clear, concise, and organized manner, using appropriate terminology. | Presents effective technical documentation, in a clear, concise, and organized manner, using appropriate terminology. | Requires some improvement in presenting technical documentation that is effective and clear. | Fails to present effective and clear technical documentation. |
| **7. Written and spoken communication in French / English** | Demonstrates strong written and spoken communication skills in both languages, effectively conveying information. | Demonstrates adequate written and spoken communication skills in both languages, conveying information with some clarity. | Requires some improvement in written and spoken communication skills in either French or English, lacking clarity in conveying ideas and information. | Fails to demonstrate satisfactory written and spoken communication skills in both French and English, lacking clarity in conveying ideas and information. |
| **8. Self-reflection** | Reflects on knowledge, skills, and demonstrates critical thinking in achieving project goals and managing stress. | Reflects on knowledge, skills, and thinks critically in achieving project goals and managing stress. | Requires outside feedback to assess their proper knowledge and skills level. | Fails to demonstrate the ability to reflect on knowledge, skills, and stress management. |

**Pass Course Criteria**

**Rubric explanations**

There are certain criteria that are considered foundational, as they represent essential aspects of the PCA and are critical to its successful completion. The criteria that form the foundation are:  

- **Solution design:** evaluates the student’s ability to analyze a problem and design a corresponding solution using scientific and programming concepts. 

- **Alignment of scientific concepts with programming goals:** assesses the student’s ability to integrate their knowledge of science and computer science through the development of computer programming solutions to scientific concepts.  

- **Implementation:** measures the student’s ability to apply computer science concepts in the implementation of a fully functional software application. 

- **Testing and troubleshooting skills:** evaluates the student’s ability to accurately validate the functioning of a software implementation and to identify, fix, and document malfunctions.   

These foundational criteria are directly associated with the program’s objectives and ensure alignment with the exit profile. 

**Following are the PASS/FAIL rules:**

- ```As a minimum requirement, if a student receives a mark of "Competent" in at least one of the foundational criteria, and “Competent, with support” for the other foundational criteria, the student should PASS the PCA. ```

- ```If a student receives a mark of "Not Yet Competent" in at least one of the foundational criteria and “Not Yet Competent” in any other criteria, then the student FAILs the PCA.  ```