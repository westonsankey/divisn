package co.divisn.destination;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "source", schema = "router")
public class Source implements Serializable {

    @Id
    @Column(name = "source_id")
    private int id;

    @Column(name = "source_type_id")
    private int sourceTypeID;

    public Source() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSourceTypeID() {
        return sourceTypeID;
    }

    public void setSourceTypeID(int sourceTypeID) {
        this.sourceTypeID = sourceTypeID;
    }
}
