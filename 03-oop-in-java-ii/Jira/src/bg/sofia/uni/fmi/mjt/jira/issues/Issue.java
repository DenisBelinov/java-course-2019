package bg.sofia.uni.fmi.mjt.jira.issues;

import bg.sofia.uni.fmi.mjt.jira.enums.IssuePriority;
import bg.sofia.uni.fmi.mjt.jira.enums.IssueResolution;
import bg.sofia.uni.fmi.mjt.jira.enums.IssueStatus;
import bg.sofia.uni.fmi.mjt.jira.enums.WorkAction;

import java.time.LocalDateTime;
import java.util.Objects;

public abstract class Issue {
    private static final int MAX_ACTION_COUNT = 20;

    private static int issueCount = 0;

    private String issueId;
    private LocalDateTime createdOn;
    private LocalDateTime lastModifiedOn;

    private int actionCount;
    private String[] actionLog;

    private IssueStatus status;
    private IssuePriority issuePriority;
    private Component component;
    private String description;

    protected IssueResolution issueResolution;

    public Issue(IssuePriority issuePriority, Component component, String description) {
        this.issuePriority = issuePriority;
        this.component = component;
        this.description = description;

        this.issueResolution = IssueResolution.UNRESOLVED;
        this.status = IssueStatus.OPEN;

        issueCount += 1;
        this.issueId = component.getShortName() + "-" + issueCount;

        this.createdOn = LocalDateTime.now();
        this.lastModifiedOn = LocalDateTime.now();

        this.actionLog = new String[MAX_ACTION_COUNT];
        this.actionCount = 0;
    }

    public String getIssueID() { return this.issueId; }

    public IssueResolution getResolution() { return this.issueResolution; }

    public IssueStatus getStatus() { return this.status; }

    public IssuePriority getPriority() { return this.issuePriority; }

    public Component getComponent() { return this.component; }

    public String getDescription() { return this.description; }

    public LocalDateTime getCreatedOn() { return createdOn; }

    public LocalDateTime getLastModifiedOn() { return lastModifiedOn; }

    public String[] getActionLog() { return this.actionLog; }

    public void setStatus(IssueStatus status) {
//        if (status == IssueStatus.RESOLVED && this.issueResolution == IssueResolution.UNRESOLVED)
//                throw new RuntimeException("Cannot change status to resolved on issue with no resolution.");

        this.status = status;
        updateModificationDate();
    }

    protected void updateModificationDate() {
        this.lastModifiedOn = LocalDateTime.now();
    }

    protected boolean hasAction(WorkAction action) {
        for (int i = 0; i < actionCount; i++) {
            if (this.actionLog[i].contains(action.toString().toLowerCase()))
                return true;
        }

        return false;
    }

    public void addAction(WorkAction action, String description) {
        if (actionCount > MAX_ACTION_COUNT) {
            String errMsg = String.format("Max action limit reached. Cannot add more actions to issue %s",
                                           this.issueId);
            throw new RuntimeException(errMsg);
        }

        this.actionLog[actionCount] = action.toString().toLowerCase() + ": " + description;
        this.actionCount += 1;
        updateModificationDate();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Issue)) return false;
        Issue issue = (Issue) o;
        return issueId.equals(issue.issueId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(issueId);
    }

    public abstract void resolve(IssueResolution resolution);
}
