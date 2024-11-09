package winestore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "wines")
@Data
@NoArgsConstructor
public class Wine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;
    private String variety;
    private Integer year;
    private String region;
    private Integer price;
    private String topnote;
    private String bottomnote;
}


//package winestore;
//
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.Table;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Entity
//@Table(name = "wines")
//@Data
//@NoArgsConstructor
//public class Wine {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    private String type;
//    private String variety;
//    private Integer year;
//    private String region;
//    private Integer price;
//    private String topnote;
//    private String bottomnote;
//}


//package winestore;
//
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.Table;
//import lombok.NoArgsConstructor;
//
//@Entity
//@Table(name = "wines")
//@NoArgsConstructor
//public class Wine {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    private String type;
//    private String variety;
//    private Integer year;
//    private String region;
//    private Integer price;
//    private String topnote;
//    private String bottomnote;
//
//    // Getters and Setters
//    public Long getId() { return id; }
//    public void setId(Long id) { this.id = id; }
//
//    public String getType() { return type; }
//    public void setType(String type) { this.type = type; }
//
//    public String getVariety() { return variety; }
//    public void setVariety(String variety) { this.variety = variety; }
//
//    public Integer getYear() { return year; }
//    public void setYear(Integer year) { this.year = year; }
//
//    public String getRegion() { return region; }
//    public void setRegion(String region) { this.region = region; }
//
//    public Integer getPrice() { return price; }
//    public void setPrice(Integer price) { this.price = price; }
//
//    public String getTopnote() { return topnote; }
//    public void setTopnote(String topnote) { this.topnote = topnote; }
//
//    public String getBottomnote() { return bottomnote; }
//    public void setBottomnote(String bottomnote) { this.bottomnote = bottomnote; }
//}
