package utilities;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataTypeConverter {
    private static final Logger logger = LoggerFactory.getLogger(DataTypeConverter.class);

    // Mapping of PostgreSQL data types to Trino equivalents
//    private static final Map<String, String> POSTGRES_TO_TRINO = new HashMap<>();
    private static final Map<String, String> ORACLE_TO_TRINO = new HashMap<>();

    static {
        /*CONVERSION ORACLE TO TRINO*/
        ORACLE_TO_TRINO.put("varchar2", "varchar");
        ORACLE_TO_TRINO.put("nvarchar2", "varchar");
        ORACLE_TO_TRINO.put("char", "char");
        ORACLE_TO_TRINO.put("nchar", "char");
        ORACLE_TO_TRINO.put("number", "integer"); // Note: Oracle's NUMBER can map to different Trino types based on precision and scale
        ORACLE_TO_TRINO.put("float", "double");
        ORACLE_TO_TRINO.put("binary_float", "real");
        ORACLE_TO_TRINO.put("binary_double", "double");
        ORACLE_TO_TRINO.put("long", "varchar");
        ORACLE_TO_TRINO.put("date", "date");
        ORACLE_TO_TRINO.put("timestamp", "timestamp");
        ORACLE_TO_TRINO.put("timestamp with time zone", "timestamp with time zone");
        ORACLE_TO_TRINO.put("timestamp with local time zone", "timestamp with time zone");
        ORACLE_TO_TRINO.put("blob", "varbinary");
        ORACLE_TO_TRINO.put("clob", "varchar");
        ORACLE_TO_TRINO.put("nclob", "varchar");
        ORACLE_TO_TRINO.put("raw", "varbinary");
        ORACLE_TO_TRINO.put("bfile", "varbinary");
        ORACLE_TO_TRINO.put("boolean", "boolean");
         /*CONVERSION POSTGRESQL TO TRINO*/
//        POSTGRES_TO_TRINO.put("character varying", "varchar");
//        POSTGRES_TO_TRINO.put("varchar", "varchar");
//        POSTGRES_TO_TRINO.put("text", "varchar");
//        POSTGRES_TO_TRINO.put("char", "char");
//        POSTGRES_TO_TRINO.put("integer", "integer");
//        POSTGRES_TO_TRINO.put("bigint", "bigint");
//        POSTGRES_TO_TRINO.put("smallint", "smallint");
//        POSTGRES_TO_TRINO.put("numeric", "decimal");
//        POSTGRES_TO_TRINO.put("decimal", "decimal");
//        POSTGRES_TO_TRINO.put("real", "real");
//        POSTGRES_TO_TRINO.put("double precision", "double");
//        POSTGRES_TO_TRINO.put("boolean", "boolean");
//        POSTGRES_TO_TRINO.put("bytea", "varbinary");
//        POSTGRES_TO_TRINO.put("json", "json");
//        POSTGRES_TO_TRINO.put("jsonb", "json");
//        POSTGRES_TO_TRINO.put("date", "date");
//        POSTGRES_TO_TRINO.put("timestamp without time zone", "timestamp");
//        POSTGRES_TO_TRINO.put("timestamp with time zone", "timestamp with time zone");

    }

    // Method to convert PostgreSQL column definitions to Trino format
    public static List<String> convertOracleToTrino(List<String> postgresColumns) {
        List<String> trinoColumns = new ArrayList<>();

        for (String column : postgresColumns) {
            String[] parts = column.split(" - "); // Split into column name and data type
            if (parts.length == 2) {
                String columnName = parts[0].trim();
                String postgresType = parts[1].trim().toLowerCase(); // Normalize data type to lowercase

                // Convert PostgreSQL type to Trino type
                String trinoType = ORACLE_TO_TRINO.getOrDefault(postgresType, "UNKNOWN");

                // Format and add to the result list
                trinoColumns.add(columnName + " - " + trinoType);
            }
        }
        return trinoColumns;
    }
}
