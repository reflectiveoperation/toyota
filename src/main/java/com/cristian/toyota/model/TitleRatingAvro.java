package com.cristian.toyota.model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TitleRatingAvro {
    private Schema schema;
    private Payload payload;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Schema {
        private StructType type;
        private List<Field> fields;
        private boolean optional;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Payload {
        private List<String> headers;
        private String message;
    }


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Field {
        private FieldType type;
        private Items items;
        private boolean optional;
        private String field;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StructType {
        private String type;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FieldType {
        private String type;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Items {
        private FieldType type;
        private boolean optional;

    }
}

