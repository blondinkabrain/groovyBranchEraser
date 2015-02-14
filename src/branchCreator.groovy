/**
 * Created by Alenka on 14.02.2015.
 */
if (args.size() < 2) {
    println "Usage: branchCreator repository-dir branchStartName"
    return
}
def repositoryDir = new File(args[0])
def startBranch = args[1]

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

def goToTestBranch = executeGitCommand('git checkout -b test')
def createBranches= {param ->

    for (int i = 0; i < 10; i++) {
        print goToTestBranch
        print executeGitCommand('git checkout -b $startBranch-$i')
    }
}
createBranches()