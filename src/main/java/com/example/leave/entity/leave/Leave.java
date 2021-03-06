package com.example.leave.entity.leave;

import com.example.leave.entity.account.Account;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Medion on 2016-09-26.
 */
@Entity
@Table(name = "leave")
@NamedQueries({
        @NamedQuery(name = "Leave.getNewID",
                query = "select max(id)+1 from Leave"),
        @NamedQuery(name = "Leave.remove",
                query = "delete from Leave where id=?1"),
        @NamedQuery(name = "Leave.findAllByAccountAndActiveAndAfterDate",
                query = "select l from Leave l where account = ?1 and active = ?2 and dateStart >= ?3"),
        @NamedQuery(name = "Leave.findAllByTypeAndAccountAndActiveAndConfirm",
                query = "select l from Leave l where leaveType = ?1 and account = ?2 and active=true and confirm=true"),
        @NamedQuery(name = "Leave.findAllByActiveAndConfirm",
                query = "select l from Leave l where active=true and confirm=true"),
        @NamedQuery(name = "Leave.findLeaveById",
                query = "select l from Leave l where id = ?1"),
        @NamedQuery(name = "Leave.findAllByAccount",
                query = "select l from Leave l where account = ?1"),
        @NamedQuery(name = "Leave.findAllByAccountAndLeaveType",
                query = "select l from Leave l where account = ?1 and leaveType = ?2"),
        @NamedQuery(name = "Leave.findAllByAccountAndActiveAndConfirmAndCheckDate",
                query = "select l from Leave l where account = ?1 and active= ?2 and confirm=?3 and dateStart <= ?4 and dateEnd >= ?4"),
})
public class Leave implements Serializable{

    @Id
    @Column(name = "leave_id")
    private Long id;

    @JoinColumn(name = "account_id", referencedColumnName = "account_id", updatable=false)
    @ManyToOne(optional = false)
    private Account account;

    @JoinColumn(name = "leave_type_id", referencedColumnName = "leave_type_id", updatable=false)
    @OneToOne(optional = false)
    private LeaveType leaveType;

    @Column(name = "datestart")
    private Date dateStart;
    @Column(name = "dateend")
    private Date dateEnd;

    @Column(name = "active")
    private Boolean active;
    @Column(name = "confirm")
    private Boolean confirm;
    @Column(name="lastyeardays")
    private int lastYearDays;
    @Column(name="currentyeardays")
    private int currentYearDays;
    @Column(name = "version")
    @Version
    private long version;

    public Leave() {
    }

    public Leave(Long id, Account account, LeaveType leaveType, Date dateStart, Date dateEnd, int lastYearDays, int  currentYearDays,Boolean active, Boolean confirm, long version) {
        this.id = id;
        this.account = account;
        this.leaveType = leaveType;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.active = active;
        this.confirm = confirm;
        this.version = version;
        this.lastYearDays = lastYearDays;
        this.currentYearDays = currentYearDays;
    }

    public Leave(Account account, LeaveType leaveType, Date dateStart,int lastYearDays, int  currentYearDays, Date dateEnd) {
        this.id = 0L;
        this.account = account;
        this.leaveType = leaveType;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.active = true;
        this.confirm = false;
        this.lastYearDays = lastYearDays;
        this.currentYearDays = currentYearDays;
        this.version = 0L;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public LeaveType getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(LeaveType leaveType) {
        this.leaveType = leaveType;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getConfirm() {
        return confirm;
    }

    public void setConfirm(Boolean confirm) {
        this.confirm = confirm;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public int getLastYearDays() {
        return lastYearDays;
    }

    public void setLastYearDays(int lastYearDays) {
        this.lastYearDays = lastYearDays;
    }

    public int getCurrentYearDays() {
        return currentYearDays;
    }

    public void setCurrentYearDays(int currentYearDays) {
        this.currentYearDays = currentYearDays;
    }
}
