package lms.demo.service;

import lms.demo.dao.MemberDAO;
import lms.demo.model.Member;

import java.util.List;

public class MemberService {

    private final MemberDAO memberDAO = new MemberDAO();
   

    // Add a new member
    public void addMember(Member member) {
        memberDAO.saveMember(member);
    }

    // Get a member by ID
    public Member getMemberById(int id) {
        return memberDAO.getMemberById(id);
    }

    // Get a list of all members
    public List<Member> getAllMembers() {
        return memberDAO.getAllMembers();
    }

    // Update an existing member
    public void updateMember(Member member) {
        memberDAO.updateMember(member);
    }

    // Delete a member by ID
    public void deleteMember(int id) {
        memberDAO.deleteMember(id);
    }
}
