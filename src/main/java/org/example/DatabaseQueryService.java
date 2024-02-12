package org.example;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DatabaseQueryService {

    private final Connection connection;

    public DatabaseQueryService(Connection connection) {
        this.connection = connection;
    }

    // Метод для читання(куріння вбиває, ніхто це не читає)
    public List<ProjectInfo> findLongestProject() {
        List<ProjectInfo> projects = new ArrayList<>();

        try {
            String sqlFilePath = "find_longest_project.sql";
            String sqlQuery = readSqlFile(sqlFilePath);

            try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        String projectName = resultSet.getString("PROJECT_NAME");
                        int monthCount = resultSet.getInt("MONTH_COUNT");

                        // Создаємо об'єкт ProjectInfo
                        ProjectInfo projectInfo = new ProjectInfo(projectName, monthCount);
                        projects.add(projectInfo);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return projects;
    }

    // Це для зчитування(свобод затриманного)
    private String readSqlFile(String sqlFilePath) throws IOException {
        return Files.readString(Path.of(sqlFilePath));
    }

    // Клас, що представляє інформацію(ну типу видумовує)
    public static class ProjectInfo {
        private final String projectName;
        private final int monthCount;

        public ProjectInfo(String projectName, int monthCount) {
            this.projectName = projectName;
            this.monthCount = monthCount;
        }

        public String getProjectName() {
            return projectName;
        }

        public int getMonthCount() {
            return monthCount;
        }
    }
}
