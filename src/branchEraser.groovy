/**
 * Created by ad.Chistyakova on 13.02.15.
 */
// Шорткат для выполнения команды консоли
// shellCommand - строка, содержащая команду
// workingDir - рабочая директория
// Возвращаемое значение: консольный вывод команды
final BRANCHES_TO_KEEP_ANYWAY = ['master', 'development', 'HEAD']

if (args.size() < 1) {
    println "Usage: branchEraser repository-dir [remove-before-date]"
    return
}
def repositoryDir = new File(args[0])

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

// Скрипт нужно выполнять из ветки 'master'
print executeGitCommand('git checkout master')

// Обновляем список удаленных веток
print executeGitCommand('git fetch')
print executeGitCommand('git remote prune origin')

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
/*
// Получение даты последнего коммита в указанной ветке
// branchName - имя ветки
// Возвращаемое значение: дата последнего коммита
def getLastCommitDate = { branchName ->
    def lastCommitDate = executeGitCommand("git log $branchName -1 --pretty=format:%ci")
    Date.parse('yyyy-M-d H:m:s Z', lastCommitDate)
}

def isTaskClosed = { branchName -> true }

*/
