package utilities;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataTypeConverter {
    private static final Logger logger = LoggerFactory.getLogger(DataTypeConverter.class);

    // Mapping of PostgreSQL data types to Trino equivalents
    private static final Map<String, String> POSTGRES_TO_TRINO = new HashMap<>();

    static {
        POSTGRES_TO_TRINO.put("character varying", "varchar");
        POSTGRES_TO_TRINO.put("varchar", "varchar");
        POSTGRES_TO_TRINO.put("text", "varchar");
        POSTGRES_TO_TRINO.put("char", "char");
        POSTGRES_TO_TRINO.put("integer", "integer");
        POSTGRES_TO_TRINO.put("bigint", "bigint");
        POSTGRES_TO_TRINO.put("smallint", "smallint");
        POSTGRES_TO_TRINO.put("numeric", "decimal");
        POSTGRES_TO_TRINO.put("decimal", "decimal");
        POSTGRES_TO_TRINO.put("real", "real");
        POSTGRES_TO_TRINO.put("double precision", "double");
        POSTGRES_TO_TRINO.put("boolean", "boolean");
        POSTGRES_TO_TRINO.put("bytea", "varbinary");
        POSTGRES_TO_TRINO.put("json", "json");
        POSTGRES_TO_TRINO.put("jsonb", "json");
        POSTGRES_TO_TRINO.put("date", "date");
        POSTGRES_TO_TRINO.put("timestamp without time zone", "timestamp");
        POSTGRES_TO_TRINO.put("timestamp with time zone", "timestamp with time zone");
    }

    // Method to convert PostgreSQL column definitions to Trino format
    public static List<String> convertPostgresToTrino(List<String> postgresColumns) {
        List<String> trinoColumns = new ArrayList<>();

        for (String column : postgresColumns) {
            String[] parts = column.split(" - "); // Split into column name and data type
            if (parts.length == 2) {
                String columnName = parts[0].trim();
                String postgresType = parts[1].trim().toLowerCase(); // Normalize data type to lowercase

                // Convert PostgreSQL type to Trino type
                String trinoType = POSTGRES_TO_TRINO.getOrDefault(postgresType, "UNKNOWN");

                // Format and add to the result list
                trinoColumns.add(columnName + " - " + trinoType);
            }
        }
        return trinoColumns;
    }
}
