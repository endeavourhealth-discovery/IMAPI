package org.endeavourhealth.imapi.parser.ecl;
// Generated from java-escape by ANTLR 4.11.1
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class ECLLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.11.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		UTF8_LETTER=1, TAB=2, LF=3, CR=4, SPACE=5, EXCLAMATION=6, QUOTE=7, POUND=8, 
		DOLLAR=9, PERCENT=10, AMPERSAND=11, APOSTROPHE=12, LEFT_PAREN=13, RIGHT_PAREN=14, 
		ASTERISK=15, PLUS=16, COMMA=17, DASH=18, PERIOD=19, SLASH=20, ZERO=21, 
		ONE=22, TWO=23, THREE=24, FOUR=25, FIVE=26, SIX=27, SEVEN=28, EIGHT=29, 
		NINE=30, COLON=31, SEMICOLON=32, LESS_THAN=33, EQUALS=34, GREATER_THAN=35, 
		QUESTION=36, AT=37, CAP_A=38, CAP_B=39, CAP_C=40, CAP_D=41, CAP_E=42, 
		CAP_F=43, CAP_G=44, CAP_H=45, CAP_I=46, CAP_J=47, CAP_K=48, CAP_L=49, 
		CAP_M=50, CAP_N=51, CAP_O=52, CAP_P=53, CAP_Q=54, CAP_R=55, CAP_S=56, 
		CAP_T=57, CAP_U=58, CAP_V=59, CAP_W=60, CAP_X=61, CAP_Y=62, CAP_Z=63, 
		LEFT_BRACE=64, BACKSLASH=65, RIGHT_BRACE=66, CARAT=67, UNDERSCORE=68, 
		ACCENT=69, A=70, B=71, C=72, D=73, E=74, F=75, G=76, H=77, I=78, J=79, 
		K=80, L=81, M=82, N=83, O=84, P=85, Q=86, R=87, S=88, T=89, U=90, V=91, 
		W=92, X=93, Y=94, Z=95, LEFT_CURLY_BRACE=96, PIPE=97, RIGHT_CURLY_BRACE=98, 
		TILDE=99;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"UTF8_LETTER", "TAB", "LF", "CR", "SPACE", "EXCLAMATION", "QUOTE", "POUND", 
			"DOLLAR", "PERCENT", "AMPERSAND", "APOSTROPHE", "LEFT_PAREN", "RIGHT_PAREN", 
			"ASTERISK", "PLUS", "COMMA", "DASH", "PERIOD", "SLASH", "ZERO", "ONE", 
			"TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE", "COLON", 
			"SEMICOLON", "LESS_THAN", "EQUALS", "GREATER_THAN", "QUESTION", "AT", 
			"CAP_A", "CAP_B", "CAP_C", "CAP_D", "CAP_E", "CAP_F", "CAP_G", "CAP_H", 
			"CAP_I", "CAP_J", "CAP_K", "CAP_L", "CAP_M", "CAP_N", "CAP_O", "CAP_P", 
			"CAP_Q", "CAP_R", "CAP_S", "CAP_T", "CAP_U", "CAP_V", "CAP_W", "CAP_X", 
			"CAP_Y", "CAP_Z", "LEFT_BRACE", "BACKSLASH", "RIGHT_BRACE", "CARAT", 
			"UNDERSCORE", "ACCENT", "A", "B", "C", "D", "E", "F", "G", "H", "I", 
			"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", 
			"X", "Y", "Z", "LEFT_CURLY_BRACE", "PIPE", "RIGHT_CURLY_BRACE", "TILDE"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, "'\\t'", "'\\n'", "'\\r'", "' '", "'!'", "'\"'", "'#'", "'$'", 
			"'%'", "'&'", "'''", "'('", "')'", "'*'", "'+'", "','", "'-'", "'.'", 
			"'/'", "'0'", "'1'", "'2'", "'3'", "'4'", "'5'", "'6'", "'7'", "'8'", 
			"'9'", "':'", "';'", "'<'", "'='", "'>'", "'?'", "'@'", "'A'", "'B'", 
			"'C'", "'D'", "'E'", "'F'", "'G'", "'H'", "'I'", "'J'", "'K'", "'L'", 
			"'M'", "'N'", "'O'", "'P'", "'Q'", "'R'", "'S'", "'T'", "'U'", "'V'", 
			"'W'", "'X'", "'Y'", "'Z'", "'['", "'\\'", "']'", "'^'", "'_'", "'`'", 
			"'a'", "'b'", "'c'", "'d'", "'e'", "'f'", "'g'", "'h'", "'i'", "'j'", 
			"'k'", "'l'", "'m'", "'n'", "'o'", "'p'", "'q'", "'r'", "'s'", "'t'", 
			"'u'", "'v'", "'w'", "'x'", "'y'", "'z'", "'{'", "'|'", "'}'", "'~'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "UTF8_LETTER", "TAB", "LF", "CR", "SPACE", "EXCLAMATION", "QUOTE", 
			"POUND", "DOLLAR", "PERCENT", "AMPERSAND", "APOSTROPHE", "LEFT_PAREN", 
			"RIGHT_PAREN", "ASTERISK", "PLUS", "COMMA", "DASH", "PERIOD", "SLASH", 
			"ZERO", "ONE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", 
			"NINE", "COLON", "SEMICOLON", "LESS_THAN", "EQUALS", "GREATER_THAN", 
			"QUESTION", "AT", "CAP_A", "CAP_B", "CAP_C", "CAP_D", "CAP_E", "CAP_F", 
			"CAP_G", "CAP_H", "CAP_I", "CAP_J", "CAP_K", "CAP_L", "CAP_M", "CAP_N", 
			"CAP_O", "CAP_P", "CAP_Q", "CAP_R", "CAP_S", "CAP_T", "CAP_U", "CAP_V", 
			"CAP_W", "CAP_X", "CAP_Y", "CAP_Z", "LEFT_BRACE", "BACKSLASH", "RIGHT_BRACE", 
			"CARAT", "UNDERSCORE", "ACCENT", "A", "B", "C", "D", "E", "F", "G", "H", 
			"I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", 
			"W", "X", "Y", "Z", "LEFT_CURLY_BRACE", "PIPE", "RIGHT_CURLY_BRACE", 
			"TILDE"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public ECLLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "ECL.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\u0004\u0000c\u018d\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002\u0001"+
		"\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004"+
		"\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007"+
		"\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b"+
		"\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002"+
		"\u000f\u0007\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002"+
		"\u0012\u0007\u0012\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002"+
		"\u0015\u0007\u0015\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002"+
		"\u0018\u0007\u0018\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a\u0002"+
		"\u001b\u0007\u001b\u0002\u001c\u0007\u001c\u0002\u001d\u0007\u001d\u0002"+
		"\u001e\u0007\u001e\u0002\u001f\u0007\u001f\u0002 \u0007 \u0002!\u0007"+
		"!\u0002\"\u0007\"\u0002#\u0007#\u0002$\u0007$\u0002%\u0007%\u0002&\u0007"+
		"&\u0002\'\u0007\'\u0002(\u0007(\u0002)\u0007)\u0002*\u0007*\u0002+\u0007"+
		"+\u0002,\u0007,\u0002-\u0007-\u0002.\u0007.\u0002/\u0007/\u00020\u0007"+
		"0\u00021\u00071\u00022\u00072\u00023\u00073\u00024\u00074\u00025\u0007"+
		"5\u00026\u00076\u00027\u00077\u00028\u00078\u00029\u00079\u0002:\u0007"+
		":\u0002;\u0007;\u0002<\u0007<\u0002=\u0007=\u0002>\u0007>\u0002?\u0007"+
		"?\u0002@\u0007@\u0002A\u0007A\u0002B\u0007B\u0002C\u0007C\u0002D\u0007"+
		"D\u0002E\u0007E\u0002F\u0007F\u0002G\u0007G\u0002H\u0007H\u0002I\u0007"+
		"I\u0002J\u0007J\u0002K\u0007K\u0002L\u0007L\u0002M\u0007M\u0002N\u0007"+
		"N\u0002O\u0007O\u0002P\u0007P\u0002Q\u0007Q\u0002R\u0007R\u0002S\u0007"+
		"S\u0002T\u0007T\u0002U\u0007U\u0002V\u0007V\u0002W\u0007W\u0002X\u0007"+
		"X\u0002Y\u0007Y\u0002Z\u0007Z\u0002[\u0007[\u0002\\\u0007\\\u0002]\u0007"+
		"]\u0002^\u0007^\u0002_\u0007_\u0002`\u0007`\u0002a\u0007a\u0002b\u0007"+
		"b\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002"+
		"\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005"+
		"\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001"+
		"\t\u0001\t\u0001\n\u0001\n\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0001"+
		"\r\u0001\r\u0001\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0001\u0010"+
		"\u0001\u0010\u0001\u0011\u0001\u0011\u0001\u0012\u0001\u0012\u0001\u0013"+
		"\u0001\u0013\u0001\u0014\u0001\u0014\u0001\u0015\u0001\u0015\u0001\u0016"+
		"\u0001\u0016\u0001\u0017\u0001\u0017\u0001\u0018\u0001\u0018\u0001\u0019"+
		"\u0001\u0019\u0001\u001a\u0001\u001a\u0001\u001b\u0001\u001b\u0001\u001c"+
		"\u0001\u001c\u0001\u001d\u0001\u001d\u0001\u001e\u0001\u001e\u0001\u001f"+
		"\u0001\u001f\u0001 \u0001 \u0001!\u0001!\u0001\"\u0001\"\u0001#\u0001"+
		"#\u0001$\u0001$\u0001%\u0001%\u0001&\u0001&\u0001\'\u0001\'\u0001(\u0001"+
		"(\u0001)\u0001)\u0001*\u0001*\u0001+\u0001+\u0001,\u0001,\u0001-\u0001"+
		"-\u0001.\u0001.\u0001/\u0001/\u00010\u00010\u00011\u00011\u00012\u0001"+
		"2\u00013\u00013\u00014\u00014\u00015\u00015\u00016\u00016\u00017\u0001"+
		"7\u00018\u00018\u00019\u00019\u0001:\u0001:\u0001;\u0001;\u0001<\u0001"+
		"<\u0001=\u0001=\u0001>\u0001>\u0001?\u0001?\u0001@\u0001@\u0001A\u0001"+
		"A\u0001B\u0001B\u0001C\u0001C\u0001D\u0001D\u0001E\u0001E\u0001F\u0001"+
		"F\u0001G\u0001G\u0001H\u0001H\u0001I\u0001I\u0001J\u0001J\u0001K\u0001"+
		"K\u0001L\u0001L\u0001M\u0001M\u0001N\u0001N\u0001O\u0001O\u0001P\u0001"+
		"P\u0001Q\u0001Q\u0001R\u0001R\u0001S\u0001S\u0001T\u0001T\u0001U\u0001"+
		"U\u0001V\u0001V\u0001W\u0001W\u0001X\u0001X\u0001Y\u0001Y\u0001Z\u0001"+
		"Z\u0001[\u0001[\u0001\\\u0001\\\u0001]\u0001]\u0001^\u0001^\u0001_\u0001"+
		"_\u0001`\u0001`\u0001a\u0001a\u0001b\u0001b\u0000\u0000c\u0001\u0001\u0003"+
		"\u0002\u0005\u0003\u0007\u0004\t\u0005\u000b\u0006\r\u0007\u000f\b\u0011"+
		"\t\u0013\n\u0015\u000b\u0017\f\u0019\r\u001b\u000e\u001d\u000f\u001f\u0010"+
		"!\u0011#\u0012%\u0013\'\u0014)\u0015+\u0016-\u0017/\u00181\u00193\u001a"+
		"5\u001b7\u001c9\u001d;\u001e=\u001f? A!C\"E#G$I%K&M\'O(Q)S*U+W,Y-[.]/"+
		"_0a1c2e3g4i5k6m7o8q9s:u;w<y={>}?\u007f@\u0081A\u0083B\u0085C\u0087D\u0089"+
		"E\u008bF\u008dG\u008fH\u0091I\u0093J\u0095K\u0097L\u0099M\u009bN\u009d"+
		"O\u009fP\u00a1Q\u00a3R\u00a5S\u00a7T\u00a9U\u00abV\u00adW\u00afX\u00b1"+
		"Y\u00b3Z\u00b5[\u00b7\\\u00b9]\u00bb^\u00bd_\u00bf`\u00c1a\u00c3b\u00c5"+
		"c\u0001\u0000\u0001\t\u0000\u00c0\u02ff\u0370\u037d\u037f\u1fff\u200c"+
		"\u200d\u2070\u218f\u2c00\u2fef\u3001\u8000\ud7ff\u8000\uf900\u8000\ufdcf"+
		"\u8000\ufdf0\u8000\ufffd\u018c\u0000\u0001\u0001\u0000\u0000\u0000\u0000"+
		"\u0003\u0001\u0000\u0000\u0000\u0000\u0005\u0001\u0000\u0000\u0000\u0000"+
		"\u0007\u0001\u0000\u0000\u0000\u0000\t\u0001\u0000\u0000\u0000\u0000\u000b"+
		"\u0001\u0000\u0000\u0000\u0000\r\u0001\u0000\u0000\u0000\u0000\u000f\u0001"+
		"\u0000\u0000\u0000\u0000\u0011\u0001\u0000\u0000\u0000\u0000\u0013\u0001"+
		"\u0000\u0000\u0000\u0000\u0015\u0001\u0000\u0000\u0000\u0000\u0017\u0001"+
		"\u0000\u0000\u0000\u0000\u0019\u0001\u0000\u0000\u0000\u0000\u001b\u0001"+
		"\u0000\u0000\u0000\u0000\u001d\u0001\u0000\u0000\u0000\u0000\u001f\u0001"+
		"\u0000\u0000\u0000\u0000!\u0001\u0000\u0000\u0000\u0000#\u0001\u0000\u0000"+
		"\u0000\u0000%\u0001\u0000\u0000\u0000\u0000\'\u0001\u0000\u0000\u0000"+
		"\u0000)\u0001\u0000\u0000\u0000\u0000+\u0001\u0000\u0000\u0000\u0000-"+
		"\u0001\u0000\u0000\u0000\u0000/\u0001\u0000\u0000\u0000\u00001\u0001\u0000"+
		"\u0000\u0000\u00003\u0001\u0000\u0000\u0000\u00005\u0001\u0000\u0000\u0000"+
		"\u00007\u0001\u0000\u0000\u0000\u00009\u0001\u0000\u0000\u0000\u0000;"+
		"\u0001\u0000\u0000\u0000\u0000=\u0001\u0000\u0000\u0000\u0000?\u0001\u0000"+
		"\u0000\u0000\u0000A\u0001\u0000\u0000\u0000\u0000C\u0001\u0000\u0000\u0000"+
		"\u0000E\u0001\u0000\u0000\u0000\u0000G\u0001\u0000\u0000\u0000\u0000I"+
		"\u0001\u0000\u0000\u0000\u0000K\u0001\u0000\u0000\u0000\u0000M\u0001\u0000"+
		"\u0000\u0000\u0000O\u0001\u0000\u0000\u0000\u0000Q\u0001\u0000\u0000\u0000"+
		"\u0000S\u0001\u0000\u0000\u0000\u0000U\u0001\u0000\u0000\u0000\u0000W"+
		"\u0001\u0000\u0000\u0000\u0000Y\u0001\u0000\u0000\u0000\u0000[\u0001\u0000"+
		"\u0000\u0000\u0000]\u0001\u0000\u0000\u0000\u0000_\u0001\u0000\u0000\u0000"+
		"\u0000a\u0001\u0000\u0000\u0000\u0000c\u0001\u0000\u0000\u0000\u0000e"+
		"\u0001\u0000\u0000\u0000\u0000g\u0001\u0000\u0000\u0000\u0000i\u0001\u0000"+
		"\u0000\u0000\u0000k\u0001\u0000\u0000\u0000\u0000m\u0001\u0000\u0000\u0000"+
		"\u0000o\u0001\u0000\u0000\u0000\u0000q\u0001\u0000\u0000\u0000\u0000s"+
		"\u0001\u0000\u0000\u0000\u0000u\u0001\u0000\u0000\u0000\u0000w\u0001\u0000"+
		"\u0000\u0000\u0000y\u0001\u0000\u0000\u0000\u0000{\u0001\u0000\u0000\u0000"+
		"\u0000}\u0001\u0000\u0000\u0000\u0000\u007f\u0001\u0000\u0000\u0000\u0000"+
		"\u0081\u0001\u0000\u0000\u0000\u0000\u0083\u0001\u0000\u0000\u0000\u0000"+
		"\u0085\u0001\u0000\u0000\u0000\u0000\u0087\u0001\u0000\u0000\u0000\u0000"+
		"\u0089\u0001\u0000\u0000\u0000\u0000\u008b\u0001\u0000\u0000\u0000\u0000"+
		"\u008d\u0001\u0000\u0000\u0000\u0000\u008f\u0001\u0000\u0000\u0000\u0000"+
		"\u0091\u0001\u0000\u0000\u0000\u0000\u0093\u0001\u0000\u0000\u0000\u0000"+
		"\u0095\u0001\u0000\u0000\u0000\u0000\u0097\u0001\u0000\u0000\u0000\u0000"+
		"\u0099\u0001\u0000\u0000\u0000\u0000\u009b\u0001\u0000\u0000\u0000\u0000"+
		"\u009d\u0001\u0000\u0000\u0000\u0000\u009f\u0001\u0000\u0000\u0000\u0000"+
		"\u00a1\u0001\u0000\u0000\u0000\u0000\u00a3\u0001\u0000\u0000\u0000\u0000"+
		"\u00a5\u0001\u0000\u0000\u0000\u0000\u00a7\u0001\u0000\u0000\u0000\u0000"+
		"\u00a9\u0001\u0000\u0000\u0000\u0000\u00ab\u0001\u0000\u0000\u0000\u0000"+
		"\u00ad\u0001\u0000\u0000\u0000\u0000\u00af\u0001\u0000\u0000\u0000\u0000"+
		"\u00b1\u0001\u0000\u0000\u0000\u0000\u00b3\u0001\u0000\u0000\u0000\u0000"+
		"\u00b5\u0001\u0000\u0000\u0000\u0000\u00b7\u0001\u0000\u0000\u0000\u0000"+
		"\u00b9\u0001\u0000\u0000\u0000\u0000\u00bb\u0001\u0000\u0000\u0000\u0000"+
		"\u00bd\u0001\u0000\u0000\u0000\u0000\u00bf\u0001\u0000\u0000\u0000\u0000"+
		"\u00c1\u0001\u0000\u0000\u0000\u0000\u00c3\u0001\u0000\u0000\u0000\u0000"+
		"\u00c5\u0001\u0000\u0000\u0000\u0001\u00c7\u0001\u0000\u0000\u0000\u0003"+
		"\u00c9\u0001\u0000\u0000\u0000\u0005\u00cb\u0001\u0000\u0000\u0000\u0007"+
		"\u00cd\u0001\u0000\u0000\u0000\t\u00cf\u0001\u0000\u0000\u0000\u000b\u00d1"+
		"\u0001\u0000\u0000\u0000\r\u00d3\u0001\u0000\u0000\u0000\u000f\u00d5\u0001"+
		"\u0000\u0000\u0000\u0011\u00d7\u0001\u0000\u0000\u0000\u0013\u00d9\u0001"+
		"\u0000\u0000\u0000\u0015\u00db\u0001\u0000\u0000\u0000\u0017\u00dd\u0001"+
		"\u0000\u0000\u0000\u0019\u00df\u0001\u0000\u0000\u0000\u001b\u00e1\u0001"+
		"\u0000\u0000\u0000\u001d\u00e3\u0001\u0000\u0000\u0000\u001f\u00e5\u0001"+
		"\u0000\u0000\u0000!\u00e7\u0001\u0000\u0000\u0000#\u00e9\u0001\u0000\u0000"+
		"\u0000%\u00eb\u0001\u0000\u0000\u0000\'\u00ed\u0001\u0000\u0000\u0000"+
		")\u00ef\u0001\u0000\u0000\u0000+\u00f1\u0001\u0000\u0000\u0000-\u00f3"+
		"\u0001\u0000\u0000\u0000/\u00f5\u0001\u0000\u0000\u00001\u00f7\u0001\u0000"+
		"\u0000\u00003\u00f9\u0001\u0000\u0000\u00005\u00fb\u0001\u0000\u0000\u0000"+
		"7\u00fd\u0001\u0000\u0000\u00009\u00ff\u0001\u0000\u0000\u0000;\u0101"+
		"\u0001\u0000\u0000\u0000=\u0103\u0001\u0000\u0000\u0000?\u0105\u0001\u0000"+
		"\u0000\u0000A\u0107\u0001\u0000\u0000\u0000C\u0109\u0001\u0000\u0000\u0000"+
		"E\u010b\u0001\u0000\u0000\u0000G\u010d\u0001\u0000\u0000\u0000I\u010f"+
		"\u0001\u0000\u0000\u0000K\u0111\u0001\u0000\u0000\u0000M\u0113\u0001\u0000"+
		"\u0000\u0000O\u0115\u0001\u0000\u0000\u0000Q\u0117\u0001\u0000\u0000\u0000"+
		"S\u0119\u0001\u0000\u0000\u0000U\u011b\u0001\u0000\u0000\u0000W\u011d"+
		"\u0001\u0000\u0000\u0000Y\u011f\u0001\u0000\u0000\u0000[\u0121\u0001\u0000"+
		"\u0000\u0000]\u0123\u0001\u0000\u0000\u0000_\u0125\u0001\u0000\u0000\u0000"+
		"a\u0127\u0001\u0000\u0000\u0000c\u0129\u0001\u0000\u0000\u0000e\u012b"+
		"\u0001\u0000\u0000\u0000g\u012d\u0001\u0000\u0000\u0000i\u012f\u0001\u0000"+
		"\u0000\u0000k\u0131\u0001\u0000\u0000\u0000m\u0133\u0001\u0000\u0000\u0000"+
		"o\u0135\u0001\u0000\u0000\u0000q\u0137\u0001\u0000\u0000\u0000s\u0139"+
		"\u0001\u0000\u0000\u0000u\u013b\u0001\u0000\u0000\u0000w\u013d\u0001\u0000"+
		"\u0000\u0000y\u013f\u0001\u0000\u0000\u0000{\u0141\u0001\u0000\u0000\u0000"+
		"}\u0143\u0001\u0000\u0000\u0000\u007f\u0145\u0001\u0000\u0000\u0000\u0081"+
		"\u0147\u0001\u0000\u0000\u0000\u0083\u0149\u0001\u0000\u0000\u0000\u0085"+
		"\u014b\u0001\u0000\u0000\u0000\u0087\u014d\u0001\u0000\u0000\u0000\u0089"+
		"\u014f\u0001\u0000\u0000\u0000\u008b\u0151\u0001\u0000\u0000\u0000\u008d"+
		"\u0153\u0001\u0000\u0000\u0000\u008f\u0155\u0001\u0000\u0000\u0000\u0091"+
		"\u0157\u0001\u0000\u0000\u0000\u0093\u0159\u0001\u0000\u0000\u0000\u0095"+
		"\u015b\u0001\u0000\u0000\u0000\u0097\u015d\u0001\u0000\u0000\u0000\u0099"+
		"\u015f\u0001\u0000\u0000\u0000\u009b\u0161\u0001\u0000\u0000\u0000\u009d"+
		"\u0163\u0001\u0000\u0000\u0000\u009f\u0165\u0001\u0000\u0000\u0000\u00a1"+
		"\u0167\u0001\u0000\u0000\u0000\u00a3\u0169\u0001\u0000\u0000\u0000\u00a5"+
		"\u016b\u0001\u0000\u0000\u0000\u00a7\u016d\u0001\u0000\u0000\u0000\u00a9"+
		"\u016f\u0001\u0000\u0000\u0000\u00ab\u0171\u0001\u0000\u0000\u0000\u00ad"+
		"\u0173\u0001\u0000\u0000\u0000\u00af\u0175\u0001\u0000\u0000\u0000\u00b1"+
		"\u0177\u0001\u0000\u0000\u0000\u00b3\u0179\u0001\u0000\u0000\u0000\u00b5"+
		"\u017b\u0001\u0000\u0000\u0000\u00b7\u017d\u0001\u0000\u0000\u0000\u00b9"+
		"\u017f\u0001\u0000\u0000\u0000\u00bb\u0181\u0001\u0000\u0000\u0000\u00bd"+
		"\u0183\u0001\u0000\u0000\u0000\u00bf\u0185\u0001\u0000\u0000\u0000\u00c1"+
		"\u0187\u0001\u0000\u0000\u0000\u00c3\u0189\u0001\u0000\u0000\u0000\u00c5"+
		"\u018b\u0001\u0000\u0000\u0000\u00c7\u00c8\u0007\u0000\u0000\u0000\u00c8"+
		"\u0002\u0001\u0000\u0000\u0000\u00c9\u00ca\u0005\t\u0000\u0000\u00ca\u0004"+
		"\u0001\u0000\u0000\u0000\u00cb\u00cc\u0005\n\u0000\u0000\u00cc\u0006\u0001"+
		"\u0000\u0000\u0000\u00cd\u00ce\u0005\r\u0000\u0000\u00ce\b\u0001\u0000"+
		"\u0000\u0000\u00cf\u00d0\u0005 \u0000\u0000\u00d0\n\u0001\u0000\u0000"+
		"\u0000\u00d1\u00d2\u0005!\u0000\u0000\u00d2\f\u0001\u0000\u0000\u0000"+
		"\u00d3\u00d4\u0005\"\u0000\u0000\u00d4\u000e\u0001\u0000\u0000\u0000\u00d5"+
		"\u00d6\u0005#\u0000\u0000\u00d6\u0010\u0001\u0000\u0000\u0000\u00d7\u00d8"+
		"\u0005$\u0000\u0000\u00d8\u0012\u0001\u0000\u0000\u0000\u00d9\u00da\u0005"+
		"%\u0000\u0000\u00da\u0014\u0001\u0000\u0000\u0000\u00db\u00dc\u0005&\u0000"+
		"\u0000\u00dc\u0016\u0001\u0000\u0000\u0000\u00dd\u00de\u0005\'\u0000\u0000"+
		"\u00de\u0018\u0001\u0000\u0000\u0000\u00df\u00e0\u0005(\u0000\u0000\u00e0"+
		"\u001a\u0001\u0000\u0000\u0000\u00e1\u00e2\u0005)\u0000\u0000\u00e2\u001c"+
		"\u0001\u0000\u0000\u0000\u00e3\u00e4\u0005*\u0000\u0000\u00e4\u001e\u0001"+
		"\u0000\u0000\u0000\u00e5\u00e6\u0005+\u0000\u0000\u00e6 \u0001\u0000\u0000"+
		"\u0000\u00e7\u00e8\u0005,\u0000\u0000\u00e8\"\u0001\u0000\u0000\u0000"+
		"\u00e9\u00ea\u0005-\u0000\u0000\u00ea$\u0001\u0000\u0000\u0000\u00eb\u00ec"+
		"\u0005.\u0000\u0000\u00ec&\u0001\u0000\u0000\u0000\u00ed\u00ee\u0005/"+
		"\u0000\u0000\u00ee(\u0001\u0000\u0000\u0000\u00ef\u00f0\u00050\u0000\u0000"+
		"\u00f0*\u0001\u0000\u0000\u0000\u00f1\u00f2\u00051\u0000\u0000\u00f2,"+
		"\u0001\u0000\u0000\u0000\u00f3\u00f4\u00052\u0000\u0000\u00f4.\u0001\u0000"+
		"\u0000\u0000\u00f5\u00f6\u00053\u0000\u0000\u00f60\u0001\u0000\u0000\u0000"+
		"\u00f7\u00f8\u00054\u0000\u0000\u00f82\u0001\u0000\u0000\u0000\u00f9\u00fa"+
		"\u00055\u0000\u0000\u00fa4\u0001\u0000\u0000\u0000\u00fb\u00fc\u00056"+
		"\u0000\u0000\u00fc6\u0001\u0000\u0000\u0000\u00fd\u00fe\u00057\u0000\u0000"+
		"\u00fe8\u0001\u0000\u0000\u0000\u00ff\u0100\u00058\u0000\u0000\u0100:"+
		"\u0001\u0000\u0000\u0000\u0101\u0102\u00059\u0000\u0000\u0102<\u0001\u0000"+
		"\u0000\u0000\u0103\u0104\u0005:\u0000\u0000\u0104>\u0001\u0000\u0000\u0000"+
		"\u0105\u0106\u0005;\u0000\u0000\u0106@\u0001\u0000\u0000\u0000\u0107\u0108"+
		"\u0005<\u0000\u0000\u0108B\u0001\u0000\u0000\u0000\u0109\u010a\u0005="+
		"\u0000\u0000\u010aD\u0001\u0000\u0000\u0000\u010b\u010c\u0005>\u0000\u0000"+
		"\u010cF\u0001\u0000\u0000\u0000\u010d\u010e\u0005?\u0000\u0000\u010eH"+
		"\u0001\u0000\u0000\u0000\u010f\u0110\u0005@\u0000\u0000\u0110J\u0001\u0000"+
		"\u0000\u0000\u0111\u0112\u0005A\u0000\u0000\u0112L\u0001\u0000\u0000\u0000"+
		"\u0113\u0114\u0005B\u0000\u0000\u0114N\u0001\u0000\u0000\u0000\u0115\u0116"+
		"\u0005C\u0000\u0000\u0116P\u0001\u0000\u0000\u0000\u0117\u0118\u0005D"+
		"\u0000\u0000\u0118R\u0001\u0000\u0000\u0000\u0119\u011a\u0005E\u0000\u0000"+
		"\u011aT\u0001\u0000\u0000\u0000\u011b\u011c\u0005F\u0000\u0000\u011cV"+
		"\u0001\u0000\u0000\u0000\u011d\u011e\u0005G\u0000\u0000\u011eX\u0001\u0000"+
		"\u0000\u0000\u011f\u0120\u0005H\u0000\u0000\u0120Z\u0001\u0000\u0000\u0000"+
		"\u0121\u0122\u0005I\u0000\u0000\u0122\\\u0001\u0000\u0000\u0000\u0123"+
		"\u0124\u0005J\u0000\u0000\u0124^\u0001\u0000\u0000\u0000\u0125\u0126\u0005"+
		"K\u0000\u0000\u0126`\u0001\u0000\u0000\u0000\u0127\u0128\u0005L\u0000"+
		"\u0000\u0128b\u0001\u0000\u0000\u0000\u0129\u012a\u0005M\u0000\u0000\u012a"+
		"d\u0001\u0000\u0000\u0000\u012b\u012c\u0005N\u0000\u0000\u012cf\u0001"+
		"\u0000\u0000\u0000\u012d\u012e\u0005O\u0000\u0000\u012eh\u0001\u0000\u0000"+
		"\u0000\u012f\u0130\u0005P\u0000\u0000\u0130j\u0001\u0000\u0000\u0000\u0131"+
		"\u0132\u0005Q\u0000\u0000\u0132l\u0001\u0000\u0000\u0000\u0133\u0134\u0005"+
		"R\u0000\u0000\u0134n\u0001\u0000\u0000\u0000\u0135\u0136\u0005S\u0000"+
		"\u0000\u0136p\u0001\u0000\u0000\u0000\u0137\u0138\u0005T\u0000\u0000\u0138"+
		"r\u0001\u0000\u0000\u0000\u0139\u013a\u0005U\u0000\u0000\u013at\u0001"+
		"\u0000\u0000\u0000\u013b\u013c\u0005V\u0000\u0000\u013cv\u0001\u0000\u0000"+
		"\u0000\u013d\u013e\u0005W\u0000\u0000\u013ex\u0001\u0000\u0000\u0000\u013f"+
		"\u0140\u0005X\u0000\u0000\u0140z\u0001\u0000\u0000\u0000\u0141\u0142\u0005"+
		"Y\u0000\u0000\u0142|\u0001\u0000\u0000\u0000\u0143\u0144\u0005Z\u0000"+
		"\u0000\u0144~\u0001\u0000\u0000\u0000\u0145\u0146\u0005[\u0000\u0000\u0146"+
		"\u0080\u0001\u0000\u0000\u0000\u0147\u0148\u0005\\\u0000\u0000\u0148\u0082"+
		"\u0001\u0000\u0000\u0000\u0149\u014a\u0005]\u0000\u0000\u014a\u0084\u0001"+
		"\u0000\u0000\u0000\u014b\u014c\u0005^\u0000\u0000\u014c\u0086\u0001\u0000"+
		"\u0000\u0000\u014d\u014e\u0005_\u0000\u0000\u014e\u0088\u0001\u0000\u0000"+
		"\u0000\u014f\u0150\u0005`\u0000\u0000\u0150\u008a\u0001\u0000\u0000\u0000"+
		"\u0151\u0152\u0005a\u0000\u0000\u0152\u008c\u0001\u0000\u0000\u0000\u0153"+
		"\u0154\u0005b\u0000\u0000\u0154\u008e\u0001\u0000\u0000\u0000\u0155\u0156"+
		"\u0005c\u0000\u0000\u0156\u0090\u0001\u0000\u0000\u0000\u0157\u0158\u0005"+
		"d\u0000\u0000\u0158\u0092\u0001\u0000\u0000\u0000\u0159\u015a\u0005e\u0000"+
		"\u0000\u015a\u0094\u0001\u0000\u0000\u0000\u015b\u015c\u0005f\u0000\u0000"+
		"\u015c\u0096\u0001\u0000\u0000\u0000\u015d\u015e\u0005g\u0000\u0000\u015e"+
		"\u0098\u0001\u0000\u0000\u0000\u015f\u0160\u0005h\u0000\u0000\u0160\u009a"+
		"\u0001\u0000\u0000\u0000\u0161\u0162\u0005i\u0000\u0000\u0162\u009c\u0001"+
		"\u0000\u0000\u0000\u0163\u0164\u0005j\u0000\u0000\u0164\u009e\u0001\u0000"+
		"\u0000\u0000\u0165\u0166\u0005k\u0000\u0000\u0166\u00a0\u0001\u0000\u0000"+
		"\u0000\u0167\u0168\u0005l\u0000\u0000\u0168\u00a2\u0001\u0000\u0000\u0000"+
		"\u0169\u016a\u0005m\u0000\u0000\u016a\u00a4\u0001\u0000\u0000\u0000\u016b"+
		"\u016c\u0005n\u0000\u0000\u016c\u00a6\u0001\u0000\u0000\u0000\u016d\u016e"+
		"\u0005o\u0000\u0000\u016e\u00a8\u0001\u0000\u0000\u0000\u016f\u0170\u0005"+
		"p\u0000\u0000\u0170\u00aa\u0001\u0000\u0000\u0000\u0171\u0172\u0005q\u0000"+
		"\u0000\u0172\u00ac\u0001\u0000\u0000\u0000\u0173\u0174\u0005r\u0000\u0000"+
		"\u0174\u00ae\u0001\u0000\u0000\u0000\u0175\u0176\u0005s\u0000\u0000\u0176"+
		"\u00b0\u0001\u0000\u0000\u0000\u0177\u0178\u0005t\u0000\u0000\u0178\u00b2"+
		"\u0001\u0000\u0000\u0000\u0179\u017a\u0005u\u0000\u0000\u017a\u00b4\u0001"+
		"\u0000\u0000\u0000\u017b\u017c\u0005v\u0000\u0000\u017c\u00b6\u0001\u0000"+
		"\u0000\u0000\u017d\u017e\u0005w\u0000\u0000\u017e\u00b8\u0001\u0000\u0000"+
		"\u0000\u017f\u0180\u0005x\u0000\u0000\u0180\u00ba\u0001\u0000\u0000\u0000"+
		"\u0181\u0182\u0005y\u0000\u0000\u0182\u00bc\u0001\u0000\u0000\u0000\u0183"+
		"\u0184\u0005z\u0000\u0000\u0184\u00be\u0001\u0000\u0000\u0000\u0185\u0186"+
		"\u0005{\u0000\u0000\u0186\u00c0\u0001\u0000\u0000\u0000\u0187\u0188\u0005"+
		"|\u0000\u0000\u0188\u00c2\u0001\u0000\u0000\u0000\u0189\u018a\u0005}\u0000"+
		"\u0000\u018a\u00c4\u0001\u0000\u0000\u0000\u018b\u018c\u0005~\u0000\u0000"+
		"\u018c\u00c6\u0001\u0000\u0000\u0000\u0001\u0000\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}