package mohsen.soltanian.example.helper

import android.content.Context
import com.amrdeveloper.codeview.Code
import com.amrdeveloper.codeview.CodeView
import mohsen.soltanian.example.R
import mohsen.soltanian.example.helper.JavaLanguage
import com.amrdeveloper.codeview.Keyword
import java.util.ArrayList
import java.util.HashSet
import java.util.regex.Pattern

object JavaLanguage {
    //Language Keywords
    private val PATTERN_KEYWORDS = Pattern.compile(
        "\\b(abstract|boolean|break|byte|case|catch" +
                "|char|class|continue|default|do|double|else" +
                "|enum|extends|final|finally|float|for|if" +
                "|implements|import|instanceof|int|interface" +
                "|long|native|new|null|package|private|protected" +
                "|public|return|short|static|strictfp|super|switch" +
                "|synchronized|this|throw|transient|try|void|volatile|while)\\b"
    )
    private val PATTERN_BUILTINS = Pattern.compile("[,:;[->]{}()]")
    private val PATTERN_SINGLE_LINE_COMMENT = Pattern.compile("//[^\\n]*")
    private val PATTERN_MULTI_LINE_COMMENT = Pattern.compile("/\\*[^*]*\\*+(?:[^/*][^*]*\\*+)*/")
    private val PATTERN_ATTRIBUTE = Pattern.compile("\\.[a-zA-Z0-9_]+")
    private val PATTERN_OPERATION =
        Pattern.compile(":|==|>|<|!=|>=|<=|->|=|>|<|%|-|-=|%=|\\+|\\-|\\-=|\\+=|\\^|\\&|\\|::|\\?|\\*")
    private val PATTERN_GENERIC = Pattern.compile("<[a-zA-Z0-9,<>]+>")
    private val PATTERN_ANNOTATION = Pattern.compile("@.[a-zA-Z0-9]+")
    private val PATTERN_TODO_COMMENT = Pattern.compile("//TODO[^\n]*")
    private val PATTERN_NUMBERS = Pattern.compile("\\b(\\d*[.]?\\d+)\\b")
    private val PATTERN_CHAR = Pattern.compile("['](.*?)[']")
    private val PATTERN_STRING = Pattern.compile("[\"](.*?)[\"]")
    private val PATTERN_HEX = Pattern.compile("0x[0-9a-fA-F]+")
    fun applyMonokaiTheme(context: Context, codeView: CodeView) {
        codeView.resetSyntaxPatternList()
        codeView.resetHighlighter()
        val resources = context.resources

        //View Background
        codeView.setBackgroundColor(resources.getColor(R.color.monokia_pro_black))

        //Syntax Colors
        codeView.addSyntaxPattern(PATTERN_HEX, resources.getColor(R.color.monokia_pro_purple))
        codeView.addSyntaxPattern(PATTERN_CHAR, resources.getColor(R.color.monokia_pro_green))
        codeView.addSyntaxPattern(PATTERN_STRING, resources.getColor(R.color.monokia_pro_orange))
        codeView.addSyntaxPattern(PATTERN_NUMBERS, resources.getColor(R.color.monokia_pro_purple))
        codeView.addSyntaxPattern(PATTERN_KEYWORDS, resources.getColor(R.color.monokia_pro_pink))
        codeView.addSyntaxPattern(PATTERN_BUILTINS, resources.getColor(R.color.monokia_pro_white))
        codeView.addSyntaxPattern(
            PATTERN_SINGLE_LINE_COMMENT,
            resources.getColor(R.color.monokia_pro_grey)
        )
        codeView.addSyntaxPattern(
            PATTERN_MULTI_LINE_COMMENT,
            resources.getColor(R.color.monokia_pro_grey)
        )
        codeView.addSyntaxPattern(PATTERN_ANNOTATION, resources.getColor(R.color.monokia_pro_pink))
        codeView.addSyntaxPattern(PATTERN_ATTRIBUTE, resources.getColor(R.color.monokia_pro_sky))
        codeView.addSyntaxPattern(PATTERN_GENERIC, resources.getColor(R.color.monokia_pro_pink))
        codeView.addSyntaxPattern(PATTERN_OPERATION, resources.getColor(R.color.monokia_pro_pink))
        //Default Color
        codeView.setTextColor(resources.getColor(R.color.monokia_pro_white))
        codeView.addSyntaxPattern(PATTERN_TODO_COMMENT, resources.getColor(R.color.gold))
        codeView.reHighlightSyntax()
    }

    fun applyNoctisWhiteTheme(context: Context, codeView: CodeView) {
        codeView.resetSyntaxPatternList()
        codeView.resetHighlighter()
        val resources = context.resources

        //View Background
        codeView.setBackgroundColor(resources.getColor(R.color.noctis_white))

        //Syntax Colors
        codeView.addSyntaxPattern(PATTERN_HEX, resources.getColor(R.color.noctis_purple))
        codeView.addSyntaxPattern(PATTERN_CHAR, resources.getColor(R.color.noctis_green))
        codeView.addSyntaxPattern(PATTERN_STRING, resources.getColor(R.color.noctis_green))
        codeView.addSyntaxPattern(PATTERN_NUMBERS, resources.getColor(R.color.noctis_purple))
        codeView.addSyntaxPattern(PATTERN_KEYWORDS, resources.getColor(R.color.noctis_pink))
        codeView.addSyntaxPattern(PATTERN_BUILTINS, resources.getColor(R.color.noctis_dark_blue))
        codeView.addSyntaxPattern(
            PATTERN_SINGLE_LINE_COMMENT,
            resources.getColor(R.color.noctis_grey)
        )
        codeView.addSyntaxPattern(
            PATTERN_MULTI_LINE_COMMENT,
            resources.getColor(R.color.noctis_grey)
        )
        codeView.addSyntaxPattern(PATTERN_ANNOTATION, resources.getColor(R.color.monokia_pro_pink))
        codeView.addSyntaxPattern(PATTERN_ATTRIBUTE, resources.getColor(R.color.noctis_blue))
        codeView.addSyntaxPattern(PATTERN_GENERIC, resources.getColor(R.color.monokia_pro_pink))
        codeView.addSyntaxPattern(PATTERN_OPERATION, resources.getColor(R.color.monokia_pro_pink))

        //Default Color
        codeView.setTextColor(resources.getColor(R.color.noctis_orange))
        codeView.addSyntaxPattern(PATTERN_TODO_COMMENT, resources.getColor(R.color.gold))
        codeView.reHighlightSyntax()
    }

    fun applyFiveColorsDarkTheme(context: Context, codeView: CodeView) {
        codeView.resetSyntaxPatternList()
        codeView.resetHighlighter()
        val resources = context.resources

        //View Background
        codeView.setBackgroundColor(resources.getColor(R.color.five_dark_black))

        //Syntax Colors
        codeView.addSyntaxPattern(PATTERN_HEX, resources.getColor(R.color.five_dark_purple))
        codeView.addSyntaxPattern(PATTERN_CHAR, resources.getColor(R.color.five_dark_yellow))
        codeView.addSyntaxPattern(PATTERN_STRING, resources.getColor(R.color.five_dark_yellow))
        codeView.addSyntaxPattern(PATTERN_NUMBERS, resources.getColor(R.color.five_dark_purple))
        codeView.addSyntaxPattern(PATTERN_KEYWORDS, resources.getColor(R.color.five_dark_purple))
        codeView.addSyntaxPattern(PATTERN_BUILTINS, resources.getColor(R.color.five_dark_white))
        codeView.addSyntaxPattern(
            PATTERN_SINGLE_LINE_COMMENT,
            resources.getColor(R.color.five_dark_grey)
        )
        codeView.addSyntaxPattern(
            PATTERN_MULTI_LINE_COMMENT,
            resources.getColor(R.color.five_dark_grey)
        )
        codeView.addSyntaxPattern(PATTERN_ANNOTATION, resources.getColor(R.color.five_dark_purple))
        codeView.addSyntaxPattern(PATTERN_ATTRIBUTE, resources.getColor(R.color.five_dark_blue))
        codeView.addSyntaxPattern(PATTERN_GENERIC, resources.getColor(R.color.five_dark_purple))
        codeView.addSyntaxPattern(PATTERN_OPERATION, resources.getColor(R.color.five_dark_purple))

        //Default Color
        codeView.setTextColor(resources.getColor(R.color.five_dark_white))
        codeView.addSyntaxPattern(PATTERN_TODO_COMMENT, resources.getColor(R.color.gold))
        codeView.reHighlightSyntax()
    }

    fun applyOrangeBoxTheme(context: Context, codeView: CodeView) {
        codeView.resetSyntaxPatternList()
        codeView.resetHighlighter()
        val resources = context.resources

        //View Background
        codeView.setBackgroundColor(resources.getColor(R.color.orange_box_black))

        //Syntax Colors
        codeView.addSyntaxPattern(PATTERN_HEX, resources.getColor(R.color.gold))
        codeView.addSyntaxPattern(PATTERN_CHAR, resources.getColor(R.color.orange_box_orange2))
        codeView.addSyntaxPattern(PATTERN_STRING, resources.getColor(R.color.orange_box_orange2))
        codeView.addSyntaxPattern(PATTERN_NUMBERS, resources.getColor(R.color.five_dark_purple))
        codeView.addSyntaxPattern(PATTERN_KEYWORDS, resources.getColor(R.color.orange_box_orange1))
        codeView.addSyntaxPattern(PATTERN_BUILTINS, resources.getColor(R.color.orange_box_grey))
        codeView.addSyntaxPattern(
            PATTERN_SINGLE_LINE_COMMENT,
            resources.getColor(R.color.orange_box_dark_grey)
        )
        codeView.addSyntaxPattern(
            PATTERN_MULTI_LINE_COMMENT,
            resources.getColor(R.color.orange_box_dark_grey)
        )
        codeView.addSyntaxPattern(
            PATTERN_ANNOTATION,
            resources.getColor(R.color.orange_box_orange1)
        )
        codeView.addSyntaxPattern(PATTERN_ATTRIBUTE, resources.getColor(R.color.orange_box_orange3))
        codeView.addSyntaxPattern(PATTERN_GENERIC, resources.getColor(R.color.orange_box_orange1))
        codeView.addSyntaxPattern(PATTERN_OPERATION, resources.getColor(R.color.gold))

        //Default Color
        codeView.setTextColor(resources.getColor(R.color.five_dark_white))
        codeView.addSyntaxPattern(PATTERN_TODO_COMMENT, resources.getColor(R.color.gold))
        codeView.reHighlightSyntax()
    }

    fun getKeywords(context: Context): Array<String> {
        return arrayOf()
    }

    fun getCodeList(context: Context): List<Code> {
        val codeList: MutableList<Code> = ArrayList()
        val keywords = getKeywords(context)
        for (keyword in keywords) {
            codeList.add(Keyword(keyword))
        }
        return codeList
    }

    val indentationStarts: Set<Char>
        get() {
            val characterSet: MutableSet<Char> = HashSet()
            characterSet.add('{')
            return characterSet
        }
    val indentationEnds: Set<Char>
        get() {
            val characterSet: MutableSet<Char> = HashSet()
            characterSet.add('}')
            return characterSet
        }
    val commentStart: String
        get() = "//"
    val commentEnd: String
        get() = ""
}