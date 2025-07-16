import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;

class User {
    private String name;
    private String email;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Email: " + email;
    }
}

class Job {
    private String title;
    private String description;
    private ArrayList<User> applicants;

    public Job(String title, String description) {
        this.title = title;
        this.description = description;
        this.applicants = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void apply(User user) {
        applicants.add(user);
        System.out.println(user.getName() + " applied for " + title);
    }

    public void apply(String name, String email) {
        User user = new User(name, email);
        applicants.add(user);
        System.out.println(user.getName() + " applied for " + title + " (via overloaded method)");
    }

    public ArrayList<User> getApplicants() {
        return applicants;
    }

    @Override
    public String toString() {
        return "Title: " + title + ", Description: " + description + ", Applicants: " + applicants.size();
    }
}

class Admin extends User {
    public Admin(String name, String email) {
        super(name, email);
    }

    public void viewUsers(ArrayList<User> users) {
        System.out.println("Registered Users:");
        for (User user : users) {
            System.out.println(user);
        }
    }

    public void viewApplications(ArrayList<Job> jobs) {
        System.out.println("Job Applications:");
        for (Job job : jobs) {
            System.out.println(job.getTitle() + " - Applicants: " + job.getApplicants().size());
            for (User user : job.getApplicants()) {
                System.out.println("  " + user);
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Job> jobs = new ArrayList<>();
        ArrayList<User> users = new ArrayList<>();
        Admin admin = new Admin("Admin", "admin@jobportal.com");

        while (true) {
            System.out.println("\nJob Portal System");
            System.out.println("1. Add a Job");
            System.out.println("2. Apply for a Job");
            System.out.println("3. Display All Jobs");
            System.out.println("4. Delete a Job");
            System.out.println("5. Admin Panel");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume leftover newline

            switch (choice) {
                case 1:
                    // Add a Job
                    System.out.print("Enter job title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter job description: ");
                    String description = scanner.nextLine();
                    Job newJob = new Job(title, description);
                    jobs.add(newJob);
                    System.out.println("Job added successfully!");

                    // Save the new job to file
                    try {
                        FileWriter writer = new FileWriter("jobs.txt", true);
                        writer.write(title + " : " + description + "\n");
                        writer.close();
                        System.out.println("Job also saved to file!");
                    } catch (IOException e) {
                        System.out.println("An error occurred while saving the job to file.");
                    }
                    break;

                case 2:
                    // Apply for a Job
                    System.out.print("Enter your Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter your Email: ");
                    String email = scanner.nextLine();
                    User user = new User(name, email);
                    users.add(user);

                    // Save the user to file
                    try {
                        FileWriter userWriter = new FileWriter("users.txt", true);
                        userWriter.write(name + " : " + email + "\n");
                        userWriter.close();
                        System.out.println("User also saved to file!");
                    } catch (IOException e) {
                        System.out.println("An error occurred while saving the user to file.");
                    }

                    System.out.print("Enter job title to apply: ");
                    String applyTitle = scanner.nextLine();
                    boolean applied = false;
                    for (Job job : jobs) {
                        if (job.getTitle().equalsIgnoreCase(applyTitle)) {
                            job.apply(user); // applying using original apply method
                            applied = true;
                            break;
                        }
                    }
                    System.out.println(applied ? "Application submitted!" : "Job not found.");
                    break;

                case 3:
                    // Display All Jobs
                    System.out.println("All Jobs:");
                    for (Job job : jobs) {
                        System.out.println(job);
                    }
                    break;

                case 4:
                    // Delete a Job
                    System.out.print("Enter job title to delete: ");
                    String deleteTitle = scanner.nextLine();
                    boolean deleted = jobs.removeIf(job -> job.getTitle().equalsIgnoreCase(deleteTitle));
                    System.out.println(deleted ? "Job deleted successfully!" : "Job not found.");
                    break;

                case 5:
                    // Admin Panel
                    System.out.println("\nAdmin Panel");
                    System.out.println("1. View Users");
                    System.out.println("2. View Job Applications");
                    System.out.print("Enter your choice: ");
                    int adminChoice = scanner.nextInt();
                    scanner.nextLine();

                    if (adminChoice == 1) {
                        admin.viewUsers(users);
                    } else if (adminChoice == 2) {
                        admin.viewApplications(jobs);
                    } else {
                        System.out.println("Invalid choice.");
                    }
                    break;

                case 6:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}