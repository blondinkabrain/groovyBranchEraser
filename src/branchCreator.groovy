/**
 * Created by Alenka on 14.02.2015.
 */
/*import groovy.time.TimeCategory
// Получение groovy-wslite для работы с Mantis API
@Grab(group='com.github.groovy-wslite', module='groovy-wslite', version='0.7.2')
import wslite.soap.**/
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
// На всякий случай, проверяем, существует ли вообще репозиторий Git по переданному пути
if (!repositoryDir.exists() || executeGitCommand('git rev-parse --git-dir').trim() != '.git') {
    System.err.println "Folder ${repositoryDir} is not valid Git repository"
    return
}

// Скрипт нужно выполнять из ветки 'master'
print executeGitCommand('git checkout master')

executeGitCommand('git checkout -b test')

def goToTestBranch = executeGitCommand('git checkout test')
def createBranches= {param ->

    for (int i = 0; i < 10; i++) {
        print goToTestBranch
        print executeGitCommand("git checkout -b $startBranch-$i")
    }
}
createBranches()
executeGitCommand('git branch -d test')
print executeGitCommand('git checkout master')