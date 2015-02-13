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