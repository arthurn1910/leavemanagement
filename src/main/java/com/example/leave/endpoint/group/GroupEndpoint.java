package com.example.leave.endpoint.group;

import com.example.leave.dto.group.ImportantDateDTO;
import com.example.leave.dto.group.TeamGroupDTO;
import com.example.leave.dto.group.UserGroupDTO;
import com.example.leave.entity.account.Account;
import com.example.leave.entity.group.ImportantDates;
import com.example.leave.entity.group.TeamGroup;
import com.example.leave.entity.group.TeamGroupMember;
import com.example.leave.entity.leave.Leave;
import com.example.leave.manager.group.GroupManager;
import com.example.leave.repository.account.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Medion on 2016-09-20.
 */
@Component
public class GroupEndpoint implements GroupEndpointInterface {
    private TeamGroup teamGroup;
    private Account account;
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    GroupManager groupManager;

    @Override
    public void createGroup(String title) {
        TeamGroup teamGroupTmp=new TeamGroup();
        teamGroupTmp.setVersion(0L);
        teamGroupTmp.setCreateDate(new java.util.Date());
        teamGroupTmp.setGroupTitle(title);
        getYourAccount();
        teamGroupTmp.setManager(this.account);
        TeamGroupMember teamGroupMember=new TeamGroupMember(this.account,teamGroupTmp);
        groupManager.createGroup(teamGroupTmp, teamGroupMember);
    }

    @Override
    public List<TeamGroup> getAllGroups() {
        return groupManager.getAllGroups();
    }

    @Override
    public List<TeamGroupDTO> getAllGroupsDTO() {
        List<TeamGroup> teamGroupList=getAllGroups();
        List<TeamGroupDTO> teamGroupDTOList=new ArrayList<>();
        for(TeamGroup teamGroup : teamGroupList){
            teamGroupDTOList.add(new TeamGroupDTO(teamGroup));
        }
        return teamGroupDTOList;

    }

    @Override
    public void joinToGroup(TeamGroupDTO teamGroupDTO) {
        getYourAccount();
        getTeamGroup(teamGroupDTO.getID());
        TeamGroupMember teamGroupMember=new TeamGroupMember(this.account, this.teamGroup);
        groupManager.joinToGroup(teamGroupMember);
    }

    @Override
    public void getYourAccount() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        this.account=accountRepository.findByLogin(user.getUsername());
    }

    @Override
    public void getTeamGroup(Long id) {
        this.teamGroup=groupManager.getTeamGroup(id);
    }

    @Override
    public List<TeamGroupMember> getTeamGroupUser(Boolean active) {
        getYourAccount();
        List<TeamGroupMember> teamGroupMemberList=groupManager.getTeamGroup(this.account,active);
        return teamGroupMemberList;
    }

    @Override
    public void acceptApplication(String login) {
        for(TeamGroupMember teamGroupMember : this.teamGroup.getTeamGroupMembers()){
            if(teamGroupMember.getEmployee().getLogin().equals(login)){
                teamGroupMember.setActive(true);
                groupManager.acceptApplication(teamGroupMember);
            }
        }
        getTeamGroup();
    }

    @Override
    public void removeMember(String login) {
        for(TeamGroupMember teamGroupMember : this.teamGroup.getTeamGroupMembers()){
            if(teamGroupMember.getEmployee().getLogin().equals(login)){
                groupManager.removeMember(teamGroupMember);
            }
        }
        getTeamGroup();
    }

    @Override
    public List<TeamGroupMember> getMemberInGroup() {
        return groupManager.getMemberInGroup(this.teamGroup);
    }

    @Override
    public TeamGroup getTeamGroup() {
        this.teamGroup=groupManager.getTeamGroup(this.teamGroup.getId());
        return this.teamGroup;
    }

    @Override
    public void setTeamGroup(TeamGroup teamGroup) {
        this.teamGroup = teamGroup;
    }

    @Override
    public Account getAccount() {
        return account;
    }

    @Override
    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public void createImportantDate(String date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date1=null;
        try {
            date1=df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ImportantDates importantDates=new ImportantDates(this.teamGroup, date1);
        groupManager.createImportantDate(importantDates);
    }

    @Override
    public void removeImportantDate(String id) {
        getTeamGroup(this.teamGroup.getId());
        for(ImportantDates importantDates : this.teamGroup.getImportantDates()){
            if(importantDates.getId()==Long.valueOf(id)) {
                groupManager.removeImportantDate(importantDates);
            }
        }
    }

    @Override
    public List<ImportantDateDTO> getImportantDates() {
        getTeamGroup(this.teamGroup.getId());
        List<ImportantDates> importantDatesList= groupManager.getImportantDates(this.teamGroup);
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date today = new Date();
        Date dateNow=null;
        try {
            dateNow = formatter.parse(formatter.format(today));
        } catch (ParseException e) {
            System.out.println("Exception getImportantDates");
        }
        List<ImportantDateDTO> importantDateDTOList =new ArrayList<>();
        for(ImportantDates importantDates : importantDatesList){
            ImportantDateDTO importantDateDTO=new ImportantDateDTO(importantDates);
            if(dateNow.before(importantDateDTO.getDate()) | dateNow.equals(importantDateDTO.getDate())){
                importantDateDTOList.add(importantDateDTO);
            }
        }
        return importantDateDTOList;

    }

    @Override
    public List<Leave> getAllLeavePlannedInGroup(TeamGroup teamGroup) {
        return groupManager.getAllLeavePlannedInGroup(teamGroup);
    }

    @Override
    public void rejectPlannedLeave(Leave leave) {
        groupManager.rejectPlannedLeave(leave);
    }

    @Override
    public UserGroupDTO getUserGroup(){
        UserGroupDTO userGroupDTO=new UserGroupDTO();
        getYourAccount();
        userGroupDTO.setAccount(this.account);
        userGroupDTO.setApplyTeamGroupDTOList(getTeamGroupDTOList(false));
        userGroupDTO.setTeamGroupDTOList(getTeamGroupDTOList(true));
        return userGroupDTO;

    }

    @Override
    public List<TeamGroupDTO> getTeamGroupDTOList(Boolean active){
        List<TeamGroupDTO> teamGroupDTOList=new ArrayList<>();
        List<TeamGroupMember> teamGroupMemberList=getTeamGroupUser(active);
        for(TeamGroupMember teamGroupMember: teamGroupMemberList){
            TeamGroup teamGroup=teamGroupMember.getTeamGroup();
            TeamGroupDTO teamGroupDTO=new TeamGroupDTO();
            teamGroupDTO.setTeamGroup(teamGroup);
            teamGroupDTOList.add(teamGroupDTO);
        }
        return teamGroupDTOList;
    }

    @Override
    public TeamGroupDTO getTeamGroupDTO(){
        TeamGroupDTO teamGroupDTO=new TeamGroupDTO(this.teamGroup);
        return teamGroupDTO;
    }

    @Override
    public void removeGroup() {
        getTeamGroup(this.teamGroup.getId());
        groupManager.removeGroup(this.teamGroup);
    }
}
