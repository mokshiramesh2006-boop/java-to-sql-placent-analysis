# EligiSync Web — MySQL, demo data, CSV and resume upload

## Requirements
- Java 17
- Eclipse IDE with Maven integration
- MySQL Server 8.x and MySQL Workbench

## 1. Create the separate database and restricted user
Run `database/schema.sql` in MySQL Workbench. Then run the following, choosing your own strong password:

```sql
CREATE USER IF NOT EXISTS 'eligisync_user'@'localhost' IDENTIFIED BY 'CHOOSE_A_STRONG_PASSWORD';
GRANT ALL PRIVILEGES ON eligisync.* TO 'eligisync_user'@'localhost';
FLUSH PRIVILEGES;
```

This application uses only `eligisync`; it does not read or change the team's `placement_analytics` database.

## 2. Set the database password (do not commit it)
Set the environment variable `DB_PASSWORD` to the password you chose. The defaults in `application.properties` are `DB_USERNAME=eligisync_user` and the local `eligisync` URL. You can override them with `DB_USERNAME` and `DB_URL` if needed. Never commit a real password to GitHub.

On Windows PowerShell, for the current terminal session:

```powershell
$env:DB_PASSWORD = "YOUR_CHOSEN_PASSWORD"
```

Launch Eclipse from that same terminal if you want Eclipse to inherit the variable, or set it in Run Configurations > Environment for the application.

## 3. Import and run in Eclipse
1. Extract the ZIP.
2. Eclipse: File > Import > Maven > Existing Maven Projects.
3. Select the extracted `EligiSync-Web` folder containing `pom.xml` and finish.
4. Right-click project > Maven > Update Project.
5. Start MySQL Server.
6. Run `EligiSyncApplication.java` as Java Application.
7. Open `http://localhost:8080`.

On first startup, the app automatically creates the JPA tables and inserts **100 fictional demonstration students and 15 fictional demonstration companies** if the respective tables are empty. It does not insert duplicates on later starts. Demo email addresses use the reserved `example.test` domain.

## 4. CSV import
Open **Import Data** in the website. `src/main/resources/static/sample-students.csv` contains 100 fictional example rows. CSV headers required: `name,studentId,cgpa`. Optional: `email,backlogs,skills,placed`. Student IDs must be unique and CGPA must be from 0 to 10. Rows with invalid data or duplicate IDs are skipped and counted.

## 5. Resume import
Upload a PDF or DOCX from **Import Data**. The prototype extracts text and makes a best-effort attempt to detect name, email, labelled CGPA/GPA and a limited set of skills. Review and correct every imported profile. Scanned/image-only PDFs require OCR, which is not included. This is heuristic extraction, not an AI guarantee.

## 6. Verify the database
In MySQL Workbench:

```sql
USE eligisync;
SELECT COUNT(*) AS student_count FROM students;
SELECT COUNT(*) AS company_count FROM companies;
```

On a fresh database, both counts should be 100 and 15. If the tables already contain data, the startup loader intentionally does not seed that table again.

## Security and project scope
This is a local academic prototype. Before real student data or public deployment, add authentication, role-based access, consent, privacy controls, file size/type and malware scanning, secure resume storage, audit logs, and robust validation. Never upload real student records or credentials to a public repository.
