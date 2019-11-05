package bg.sofia.uni.fmi.mjt.jira;

import bg.sofia.uni.fmi.mjt.jira.enums.IssueResolution;
import bg.sofia.uni.fmi.mjt.jira.enums.WorkAction;
import bg.sofia.uni.fmi.mjt.jira.interfaces.Filter;
import bg.sofia.uni.fmi.mjt.jira.interfaces.Repository;
import bg.sofia.uni.fmi.mjt.jira.issues.Issue;

public class Jira implements Filter, Repository {
    private static final int MAX_ISSUE_COUNT = 100;

    private Issue[] issues;
    private int issuesCount;

    public Jira() {
        this.issues = new Issue[MAX_ISSUE_COUNT];
        this.issuesCount = 0;
    }

    @Override
    public Issue find(String issueID) {
        for (int i = 0; i < issuesCount; i++) {
            if (issues[i].getIssueID() == issueID)
                return issues[i];
        }

        return null;
    }

    @Override
    public void addIssue(Issue issue) {
        if (issuesCount > MAX_ISSUE_COUNT) {
            throw new RuntimeException("Max issues limit reached. Cannot add more issues to Jira");
        }

        if (find(issue.getIssueID()) != null) {
            throw new RuntimeException("Issue already present in Jira");
        }
        this.issues[issuesCount] = issue;
        this.issuesCount += 1;
    }

    public void addActionToIssue(Issue issue, WorkAction action, String actionDescription) {
        issue.addAction(action, actionDescription);
    }

    public void resolveIssue(Issue issue, IssueResolution resolution) {
        issue.resolve(resolution);
    }
}
