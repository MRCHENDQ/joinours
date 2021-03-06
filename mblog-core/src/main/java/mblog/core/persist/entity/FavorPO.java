package mblog.core.persist.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * @author cdq2016/8/31.
 */
@Entity
@Table(name = "mto_favors")
public class FavorPO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "own_id")
    private long ownId;

    @Column(name = "post_id")
    private long postId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOwnId() {
        return ownId;
    }

    public void setOwnId(long ownId) {
        this.ownId = ownId;
    }

    public long getPostId() {
        return postId;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
