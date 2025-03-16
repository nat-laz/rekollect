package org.example.rekollectapi.model.index;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.example.rekollectapi.config.dateandtime.InstantToStringSerializer;
import org.example.rekollectapi.config.dateandtime.StringToInstantDeserializer;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.time.Instant;
import java.util.List;

@Data
@Document(indexName = "records")
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecordIndex {

    @Id
    private String id;  // elasticsearch IDs are Strings

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Keyword)
    private String category;

    @Field(type = FieldType.Date, format = DateFormat.date)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String releaseDate;
    ;

    @Field(type = FieldType.Date, format = DateFormat.date_optional_time)
    @JsonSerialize(using = InstantToStringSerializer.class)
    @JsonDeserialize(using = StringToInstantDeserializer.class)
    private Instant createdAt;

    @Field(type = FieldType.Date, format = DateFormat.date_optional_time)
    @JsonSerialize(using = InstantToStringSerializer.class)
    @JsonDeserialize(using = StringToInstantDeserializer.class)
    private Instant updatedAt;

    @Field(type = FieldType.Keyword)
    private List<String> tags;

    @Field(type = FieldType.Nested) // nested object for creators
    private List<CreatorIndex> creators;
}
