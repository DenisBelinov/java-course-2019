package bg.sofia.uni.fmi.mjt.jira;

import bg.sofia.uni.fmi.mjt.jira.enums.IssuePriority;
import bg.sofia.uni.fmi.mjt.jira.enums.IssueResolution;
import bg.sofia.uni.fmi.mjt.jira.enums.IssueStatus;
import bg.sofia.uni.fmi.mjt.jira.enums.WorkAction;
import bg.sofia.uni.fmi.mjt.jira.issues.Bug;
import bg.sofia.uni.fmi.mjt.jira.issues.Component;

public class Main {

    public static void main(String[] args) {
        Jira jira = new Jira();

        Bug bug = new Bug(IssuePriority.BLOCKER, new Component("Issue Tracker", "IT"), "Bugs ain't workin");


        jira.addIssue(bug);

        jira.addActionToIssue(bug, WorkAction.FIX, "I am trying to fix this okay");
        jira.addActionToIssue(bug, WorkAction.TESTS, "I am trying to test this okay");

        bug.setStatus(IssueStatus.RESOLVED);
        jira.resolveIssue(bug, IssueResolution.FIXED);

    }
}
