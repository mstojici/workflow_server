# Workflow Server
## Available Commands
```text
create_workflow <workflow_name> <path_to_JSON_workflow>
start_session <session_name> <workflow_name>
resume_session <session_name>
session_state <session_name>
set_exec_time <time_in_seconds>
```

## Usage examples
### Workflow provided with exercise
```text
PS C:\Users\miljac\Documents> java -jar .\workflowserver.jar
WorkflowServer is running.
> create_workflow wf01 workflow_definition.json
Workflow wf01 loaded.
> set_exec_time 5
Execution time set to 5 seconds.
> start_session session01 wf01
Session session01 started.
Session session01: Task1 done.
Session session01: Task2 done.
Session session01: Waiting on lane lane2

> resume_session session01
Resuming session session01.
> session_state session01
Task3 in progress.
Session session01: Task3 done.
Session session01: Waiting on lane lane3

> resume_session session01
Resuming session session01.
Session session01: NOP done.
Session session01: Task4 done.
Session session01 completed.

> stop
WorkflowServer stopped.
```

### Cyclic Workflow
```text
PS C:\Users\miljac\Documents> java -jar .\workflowserver.jar
WorkflowServer is running.
> create_workflow wf02 C:\Users\miljac\Documents\workflow_definition_cyclic.json
Workflow must be acyclic.
> stop
WorkflowServer stopped.
```

### Workflow with e-mail and timeout node
```text
PS C:\Users\miljac\Documents> java -jar .\workflowserver.jar
WorkflowServer is running.
> create_workflow wf02 C:\Users\miljac\Documents\workflow_definition_timer.json
Workflow wf02 loaded.
> set_exec_time 7
Execution time set to 7 seconds.
> start_session session01 wf02
Session session01 started.
Session session01: Task1 done.

> session_state session01
Sending e-mail to : name@organisation.org
Session session01: Email sent to name@organisation.org.
Session session01: Waiting on lane lane2
Session session01: Timeout occured, proceeding with alternate task...
Session session01: Task5 done.
Session session01: NOP done.
Session session01: Waiting on lane lane2

> resume_session session01
Session session01: Timer killed, resuming...
Session session01: Task3 done.
Session session01: Waiting on lane lane3

> resume_session session01
Resuming session session01.
Session session01: NOP done.
Session session01: Task4 done.
Session session01 completed.

> stop
WorkflowServer stopped.
```

