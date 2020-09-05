package pe.gadolfolozano.listdetailroombackupapp.data.entity

fun TaskDetailEntity.mapToBackup() = BackupTaskDetailEntity(
    uuid = uuid,
    taskId = taskId,
    detail = detail
)

fun BackupTaskDetailEntity.mapToEntity() = TaskDetailEntity(
    uuid = uuid,
    taskId = taskId,
    detail = detail
)
