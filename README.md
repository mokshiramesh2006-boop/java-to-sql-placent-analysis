# EligiSync Web — College Placement Analytics & Eligibility Engine

A Spring Boot web application prototype for the EligiSync PBL project. It includes a browser dashboard, student and company management, configurable eligibility checks, candidate matching/ranking, skill-gap analysis, and print-friendly reports.

## Important relationship to the existing project

This is a **separate web project** and does not overwrite the existing Swing application. The GitHub screenshot showed Java files such as `DBConnection.java`, `DashboardFrame.java`, `LoginFrame.java`, `ReadStudent.java`, and `StudentDetailsFrame.java`. Those original files have not been modified or silently copied into this project. Review them before integrating existing login/database logic.

## Requirements

- JDK 17 or newer
- Eclipse IDE with Maven support (Eclipse IDE for Enterprise Java and Web Developers is convenient)
- Internet access on first run so Maven can download dependencies

## Import into Eclipse

1. Extract `EligiSync-Web.zip` to a normal folder.
2. In Eclipse, choose **File → Import → Maven → Existing Maven Projects**.
3. Select the extracted `EligiSync-Web` folder.
4. Select `pom.xml`, then click **Finish**.
5. Wait for Maven dependencies to finish downloading.
6. Open `src/main/java/com/eligisync/EligiSyncApplication.java`.
7. Right-click the class → **Run As → Java Application** (or **Spring Boot App**, if shown).
8. Visit http://localhost:8080

## Features

- Dashboard with student, company, placement and placement-rate metrics
- Student CRUD and search
- Company/role CRUD with minimum CGPA, maximum backlog, required skills and package
- Company-specific eligibility checks
- Candidate ranking with a transparent demo matching score
- Skill-gap review for students who do not qualify
- Reports with print / Save as PDF
- H2 file-backed database that persists in `./data/eligisync`

## Demo matching score

The current illustrative score is 60% CGPA, 25% required-skill coverage, and 15% backlog status. The hard eligibility rule requires the student's CGPA to meet the company's minimum, backlogs to be at or below its maximum, and all listed required skills to be present. Confirm the score formula and rules with your group/teacher before treating them as the project's final policy.

## Database

This prototype uses H2 for easier setup. It does not automatically connect to any existing MySQL/SQLite database. Once the original `DBConnection.java` and database schema are available, integrate or migrate deliberately rather than overwriting data.

The H2 console is enabled at `/h2-console` for local development. JDBC URL: `jdbc:h2:file:./data/eligisync;AUTO_SERVER=TRUE`; username `sa`; blank password. Do not expose the H2 console publicly.

## Deploying

This is a server-side Java application. GitHub Pages alone cannot run it. Deploy to a host that supports Java/Spring Boot and configure a persistent database. For production, disable the H2 console, configure authentication/authorization, HTTPS, backups, and secrets through environment variables.

## Git

Keep the original group project safe. Add this as a new folder/repository or branch only after agreeing with teammates. Do not commit real student personal data, database passwords, or secrets.


See `README-SETUP-MYSQL-IMPORT.md` for MySQL, CSV and PDF/DOCX import setup.
