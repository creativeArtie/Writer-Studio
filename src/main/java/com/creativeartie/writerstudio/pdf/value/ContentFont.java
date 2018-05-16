package com.creativeartie.writerstudio.pdf.value;

import java.awt.*; 
import java.io.*; 
import java.util.Optional;
import java.util.Objects;
import java.util.function.Function;

import com.google.common.base.*;

import org.apache.pdfbox.pdmodel.font.*;

import static com.creativeartie.writerstudio.main.Checker.*;

/** Information about the font, to be filled by sub-classes. */
public abstract class ContentFont<T>{

    /** Describes what field is changed. 
     * 
     * Using constructor the normal way was to long and prone to 
     * mistakes. Making builder class is too much work. 
     */
    protected enum Key {
		/** Font family changed. */             FONT,
		/** Font size changed. */               SIZE, 
		/** Font color changed. */              COLOR, 
		/** Font bold format changed. */        BOLD, 
		/** Font italics format changed. */     ITALICS, 
		/** Font underline format changed. */   LINED, 
		/** Font superscript format changed. */ SUPER;
        
    }

    private final T textFont;
    private final boolean textMono;
    private final int textSize;
    private final Color textColor;
    private final boolean textBold;
    private final boolean textItalics;
    private final boolean textLined;
    private final boolean textSuper;

	/** Creating the first {@link ContentFont}. */
    protected ContentFont(){
        this(false, 12, Color.BLACK, false, false, false, false);
    }

	/** Creating the updated {@link ContentFont}. 
	 * 
	 * @param old
	 * 		old {@linkplain ContentFont}.
	 * @param edit
	 * 		edited field
	 * @param replace 
	 * 		replace value
	 */
    protected ContentFont(ContentFont<T> old, Key edit, Object replace){
        textColor   = edit == Key.COLOR?   (Color)   replace: old.textColor;
        textSize    = edit == Key.SIZE?    (Integer) replace: old.textSize;
        textBold    = edit == Key.BOLD?    (Boolean) replace: old.textBold;
        textItalics = edit == Key.ITALICS? (Boolean) replace: old.textItalics;
        textLined   = edit == Key.LINED?   (Boolean) replace: old.textLined;
        textSuper   = edit == Key.SUPER?   (Boolean) replace: old.textSuper;
        if (Key.FONT == edit){
            /// Replaces font families
            textMono = (Boolean)replace;
            textFont = buildFont(textMono, textBold, 
				textItalics, textMono);
        } else if (Key.ITALICS == edit || Key.BOLD == edit || 
				Key.SUPER == edit){
            /// Replaces font due to bold or italics, etc.
            textMono = old.textMono;
            textFont = buildFont(textMono, textBold, 
				textItalics, textSuper);
        } else {
			/// No font changes
            textMono = old.textMono;
            textFont = old.textFont;
        }
    }

    private ContentFont(boolean mono, int size, Color color,
            boolean bold, boolean italics, boolean underline, boolean superscript){
        textFont = buildFont(mono, bold, italics, superscript);
        textMono = mono;
        textSize = size;
        textColor = color;
        textBold = bold;
        textItalics = italics;
        textLined = underline;
        textSuper = superscript;
    }
    
	/** Gets the new font.
	 * 
	 * @param mono
	 * 		is mono text
	 * @param bold
	 * 		is bold text
	 * @param italics
	 * 		is italics text
	 * @param superscript
	 * 		is superscript text
	 * @return answer
	 */
    protected abstract T buildFont(boolean mono, boolean bold, 
		boolean italics, boolean superscript);

    public T getFont(){
        return textFont;
    }

    public int getSize(){
        return textSize;
    }

    public Color getColor(){
        return textColor;
    }

    public ContentFont changeSize(int size){
        if (size == textSize){
            return this;
        }
        Object key = new Object();
        return produce(this, Key.SIZE, 12);
    }

    public ContentFont changeToSerif(){
        return produce(this, Key.FONT, false);
    }

    public ContentFont changeToMono(){
        return produce(this, Key.FONT, true);
    }

    public ContentFont changeFontColor(Color color){
        checkNotNull(color, "color");
        return produce(this, Key.COLOR, color);
    }

    public ContentFont changeBold(boolean b){
        return produce(this, Key.BOLD, b);
    }

    public ContentFont changeItalics(boolean b){
        return produce(this, Key.ITALICS, b);
    }

    public ContentFont changeUnderline(boolean b){
        return produce(this, Key.LINED, b);
    }

    public ContentFont changeToSuperscript(){
        return produce(this, Key.SUPER, true);
    }

    public ContentFont changeToNoramlScript(){
        return produce(this, Key.SUPER, false);
    }

    public abstract ContentFont produce(ContentFont font, Key key,
        Object value);

    public abstract float getWidth(String text) throws IOException;
    public abstract float getHeight() throws IOException;

    public boolean isSuperscript(){
        return textSuper;
    }

    public boolean isUnderline(){
        return textLined;
    }

    @Override
    public String toString(){
        return MoreObjects.toStringHelper(this)
            .add("font", textFont)
            .add("size", textSize)
            .add("color", textColor)
            .toString();
    }


    @Override
    public boolean equals(Object obj){
        if (obj != null && obj instanceof ContentFont){
            ContentFont other = (ContentFont) obj;
            return Objects.equals(textFont, other.textFont) &&
                textSize == other.textSize &&
                textSuper == other.textSuper &&
                Objects.equals(textColor, other.textColor);
        }
        return false;
    }

    @Override
    public int hashCode(){
        return Objects.hash(textFont, textSize, textSuper, textColor);
    }

}
