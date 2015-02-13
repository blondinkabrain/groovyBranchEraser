/**
 * Created by ad.Chistyakova on 13.02.15.
 */
// Шорткат для выполнения команды консоли
// shellCommand - строка, содержащая команду
// workingDir - рабочая директория
// Возвращаемое значение: консольный вывод команды
def executeShellCommand = { shellCommand, workingDir ->
    def shellCommandProc = shellCommand.execute(null, workingDir)

    def out = new StringBuilder()
    def err = new StringBuilder()
    shellCommandProc.waitForProcessOutput(out, err)

    if (err) {
        // вывод команды в поток ошибок использовать вряд ли получится,
        // так что просто выводим его в консоль для информации
        System.err.print err.toString()
    }

    out.toString()
}

println "Hello, World!"

// Шорткат для выполнения команды Git
// gitCommand - строка, содержащая команду
// Возвращаемое значение: консольный вывод команды
def executeGitCommand = { gitCommand ->
    executeShellCommand(gitCommand, repositoryDir)
}

def mergedBranchesOutput = executeGitCommand('git branch --merged master')
mergedBranchesOutput.eachLine { branchLine ->
    // Определение имени локальной ветки с помощью регулярного выражения
    def matcher = branchLine =~ /^\s*\*?\s*([^\s]*)$/
    if (!matcher) {
        return
    }
    def branch = matcher[0][1]

    if (// Если вообще можно удалять ветку с таким именем
    !BRANCHES_TO_KEEP_ANYWAY.contains(branch) &&
            // в ветке не было активности после указанной даты
            getLastCommitDate(branch) < removeBeforeDate &&
            // и задача закрыта в багтрекере
            isTaskClosed(branch)) {

        // Удаление локальной ветки
        print executeGitCommand("git branch -d $branch")
    }
}