Rabobank Customer Statement Processor

Rabobank Customer Statement Processor is a spring boot comprising both web and batch application. 
The application uses Spring Batch and Thymeleaf template engine to display html files. 
The first page displays option to upload either a CSV or XML file. When the user clicks submit button the file gets loaded and validated. 
The uploaded file if passes through successful validation is pushed to the /temp folder if the underlying OS is windows and /var/temp if the underlying OS is linux.
The page redirects to fileupload status page where the file validation status is displayed.
It also provides an option to trigger a job if the file validation is  successful.
The reports are generated in /temp folder for Windows and /var/temp for linux.



## Technologies
Project is created with:

Java 8
STS (Spring Tool Suite) IDE
Maven 3.x
Tomcat Server (in-built Sping Boot server)
Spring Boot
Spring Batch
Thymeleaf
H2 inmemory datbase to store spring generated tables
Junit

This project is built using STS ide and Maven dependency management. The project can be directly imported in STS ide or can be run from the 
command line using maven command. The application uses inbuilt spring boot tomcat server can be accessed using the url http://localhost:8080


## COMPILE and RUN IN AN IDE 

Import the project in STS IDE. 
Go to Project Explorer. 
Right click the project root folder -> Maven -> clean
Right click the project root folder -> Maven -> install
Right click the project root folder -> Maven -> Spring Boot App


---- RUN FROM COMMANDLINE ----
Unzip the file RaboCustStmtProcessor-1.0.0.zip
Place the Jar in a directory.
Run the following command from the same place

java -jar RaboCustStmtProcessor-1.0.0.jar
