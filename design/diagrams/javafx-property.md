# WritingScene
- writingText
    - -> TextPane.loadText()
- writingStat
    - => WritingScene.writingStat
    - -> TextPane.loadStat()
- writingData
    - -> WindowMatter.setData()
- lastSelected
    - -> TextPane.spanSelected()
- refocusText 
    - -> refocus()
- scene.focusOwner
    - -> refocus()
- (Main) -> setWritingFile() 
    - -> mainMenuBar.writingFile
- refoucs() 
    - -> refocusText

# WritingStat
- writingStat
    - -> setStat
    - -> WindowStatMonth.setStat
    - -> WindowStatDay.setStat
- wordSpinner.value
    - -> editWord
- hourSpinner.value
    - -> editHours
- minuteSpinner.value
    - -> editMinutes
- setStat()
    - -> updateStat()
- updateStat()
    - -> wordSpinner.value
    - -> hourSpinner.value
    - -> minuteSpinner.value
- editWords()
- editHours()
    - -> editTime()
- editMinutes()
    - -> editTime()
- editTime()

# WindowStatMonth
- currentMonth
    - -> setMonth()
- firstButtom.onAction 
    - -> toFirstMonth()
- pastButton.onAction 
    - -> toPastMonth()
- nextButtom.onAction 
    - -> toNextMonth()
- endButton.onAction 
    - -> toEndMonth()
- setStat() 
    - -> currentMonth
- setMonth() 
    - -> endButton.disable
    - -> nextButton.disable
    - -> firstButton.disable
    - -> pastButton.disable
    - -> WindowStatDay.showDay
- -> toFirstMonth()
    - currentMonth
- -> toPastMonth()
    - currentMonth
- -> toNextMonth()
    - currentMonth
- -> toEndMonth()
    - currentMonth

# WindowStatDay
- showDay
   - -> setDay() 
- setStat()
    - -> setDay()
- setStat()
    - -> statLabel.graphic
    - -> statTip.text

# WindowMatter
- showMatter
    - -> showMatter()
    - -> textArea.plainText
- textArea.plainText
    - -> updateText()
- updateText()
    - -> updatePreview()
- updatePreview() 
    - -> previewText.children

# TextPane
- (timer)
    -> updateTime()
- textReady
- textArea.position 
    - -> caretMoved()
- textArea.plainText 
    - -> textChanged()
- caretMoved()
    - -> lineTypeLabel.text
- 

# MainMenuBar
- writingFile
    - => WritingScene.writingText
    - => WritingScene.writingData
    - => WritingScene.writingStat
