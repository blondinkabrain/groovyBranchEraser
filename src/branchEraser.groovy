/**
 * Created by ad.Chistyakova on 13.02.15.
 */
// Шорткат для выполнения команды консоли
// shellCommand - строка, содержащая команду
// workingDir - рабочая директория
// Возвращаемое значение: консольный вывод команды
final BRANCHES_TO_KEEP_ANYWAY = ['master', '*master', 'development','*development', 'HEAD']//Со * смешной хак

if (args.size() < 2) {
    println "Usage: branchEraser repository-dir branchStartName"
    return
}
def repositoryDir = new File(args[0])
def startBranch = args[1].trim()

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

def mergedBranchesOutput = executeGitCommand('git branch')//('git branch --merged master')
mergedBranchesOutput.eachLine { branchLine ->
    branchLine = branchLine.replaceAll(" ", "")

//    println branchLine.matches(/^$startBranch.*/) // тоже работает
    def matchesBool =  branchLine.startsWith(startBranch)

    if ( matchesBool &&
    // Если вообще можно удалять ветку с таким именем
    !BRANCHES_TO_KEEP_ANYWAY.contains(branchLine)) {

        // Удаление локальной ветки
        print executeGitCommand("git branch -d $branchLine")
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
