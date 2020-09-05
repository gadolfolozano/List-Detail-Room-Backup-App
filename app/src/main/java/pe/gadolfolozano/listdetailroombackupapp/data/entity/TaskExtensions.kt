package pe.gadolfolozano.listdetailroombackupapp.data.entity

fun TaskEntity.mapToBackup() = BackupTaskEntity(
    uuid = uuid,
    name = name
)

fun BackupTaskEntity.mapToEntity() = TaskEntity(
    uuid = uuid,
    name = name
)