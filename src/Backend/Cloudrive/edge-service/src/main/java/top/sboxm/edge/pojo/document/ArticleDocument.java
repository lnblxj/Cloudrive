package top.sboxm.edge.pojo.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Document(indexName = "articles")
public class ArticleDocument {

    @Id
    private Long id;

    @Field(type = FieldType.Text, analyzer = "ik_smart")
    private String title;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String content;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String summary;
}