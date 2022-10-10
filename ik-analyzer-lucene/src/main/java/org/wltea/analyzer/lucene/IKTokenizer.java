/**
 * IK 中文分词  版本 5.0
 * IK Analyzer release 5.0
 * <p>
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p>
 * 源代码由林良益(linliangyi2005@gmail.com)提供
 * 版权声明 2012，乌龙茶工作室
 * provided by Linliangyi and copyright 2012 by Oolong studio
 */
package org.wltea.analyzer.lucene;

import java.io.IOException;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

/**
 * IK分词器 Lucene Tokenizer适配器类 兼容Lucene 3.1以上版本
 */
public final class IKTokenizer extends Tokenizer {

    // IK分词器实现
    private IKSegmenter _IKImplement;
    // 词元文本属性
    private CharTermAttribute termAtt;
    // 词元位移属性
    private OffsetAttribute offsetAtt;
    // 记录最后一个词元的结束位置
    private int finalOffset;

    /**
     * Lucene 3.5 Tokenizer适配器类构造函数
     *
     * @param useSmart 是否使用智能模式
     */
    public IKTokenizer(boolean useSmart) {
        super();
        this.offsetAtt = addAttribute(OffsetAttribute.class);
        this.termAtt = addAttribute(CharTermAttribute.class);
        this._IKImplement = new IKSegmenter(this.input, useSmart);
    }

    /* (non-Javadoc)
     * @see org.apache.lucene.analysis.TokenStream#incrementToken()
     */
    @Override
    public boolean incrementToken() throws IOException {
        // 清除所有的词元属性
        clearAttributes();
        Lexeme nextLexeme = this._IKImplement.next();
        if (nextLexeme != null) {
            // 将Lexeme转成Attributes
            // 设置词元文本
            this.termAtt.append(nextLexeme.getLexemeText());
            // 设置词元长度
            this.termAtt.setLength(nextLexeme.getLength());
            // 设置词元位移
            this.offsetAtt.setOffset(nextLexeme.getBeginPosition(), nextLexeme.getEndPosition());
            // 记录分词的最后位置
            this.finalOffset = nextLexeme.getEndPosition();
            // 返会true告知还有下个词元
            return true;
        }
        // 返会false告知词元输出完毕
        return false;
    }

    /*
     * (non-Javadoc)
     * @see org.apache.lucene.analysis.Tokenizer#reset(java.io.Reader)
     */
    @Override
    public void reset() throws IOException {
        super.reset();
        this._IKImplement.reset(this.input);
    }

    @Override
    public final void end() {
        // set final offset
        this.offsetAtt.setOffset(this.finalOffset, this.finalOffset);
    }
}
