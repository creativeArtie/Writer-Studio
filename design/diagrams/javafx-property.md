# WritingScene
- writingText
- writingStat
    - => WritingScene.writingStat
- writingData
- lastSelected
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


# MainMenuBar
- writingFile
    - => WritingScene.writingText
    - => WritingScene.writingData
    - => WritingScene.writingStat
