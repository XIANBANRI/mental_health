package com.sl.mentalhealth.service;

import com.sl.mentalhealth.dto.AssessmentScaleUpdateRequest;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AssessmentScaleExcelParserService {

  public List<AssessmentScaleUpdateRequest.QuestionDTO> parseQuestionExcel(MultipartFile file) throws Exception {
    List<AssessmentScaleUpdateRequest.QuestionDTO> result = new ArrayList<>();

    try (InputStream inputStream = file.getInputStream();
        Workbook workbook = WorkbookFactory.create(inputStream)) {

      Sheet sheet = workbook.getSheetAt(0);
      for (int i = 1; i <= sheet.getLastRowNum(); i++) {
        Row row = sheet.getRow(i);
        if (row == null) {
          continue;
        }

        Integer questionNo = getIntValue(row.getCell(0));
        String questionText = getStringValue(row.getCell(1));

        if (questionNo == null || questionText == null || questionText.isBlank()) {
          continue;
        }

        AssessmentScaleUpdateRequest.QuestionDTO questionDTO =
            new AssessmentScaleUpdateRequest.QuestionDTO();
        questionDTO.setQuestionNo(questionNo);
        questionDTO.setQuestionText(questionText.trim());

        List<AssessmentScaleUpdateRequest.OptionDTO> options = new ArrayList<>();
        int optionNo = 1;
        for (int c = 2; c <= 6; c++) {
          String optionText = getStringValue(row.getCell(c));
          if (optionText == null || optionText.isBlank() || "—".equals(optionText.trim())) {
            continue;
          }

          AssessmentScaleUpdateRequest.OptionDTO optionDTO =
              new AssessmentScaleUpdateRequest.OptionDTO();
          optionDTO.setOptionNo(optionNo);
          optionDTO.setOptionText(optionText.trim());
          optionDTO.setOptionScore(optionNo - 1);
          options.add(optionDTO);
          optionNo++;
        }

        questionDTO.setOptions(options);
        result.add(questionDTO);
      }
    }
    return result;
  }

  public List<AssessmentScaleUpdateRequest.RuleDTO> parseRuleExcel(MultipartFile file) throws Exception {
    List<AssessmentScaleUpdateRequest.RuleDTO> result = new ArrayList<>();

    try (InputStream inputStream = file.getInputStream();
        Workbook workbook = WorkbookFactory.create(inputStream)) {

      Sheet sheet = workbook.getSheetAt(0);
      for (int i = 1; i <= sheet.getLastRowNum(); i++) {
        Row row = sheet.getRow(i);
        if (row == null) {
          continue;
        }

        Integer minScore = getIntValue(row.getCell(0));
        Integer maxScore = getIntValue(row.getCell(1));
        String resultLevel = getStringValue(row.getCell(2));
        String resultSummary = getStringValue(row.getCell(3));
        String suggestion = getStringValue(row.getCell(4));

        if (minScore == null || maxScore == null || resultLevel == null || resultSummary == null) {
          continue;
        }

        AssessmentScaleUpdateRequest.RuleDTO ruleDTO = new AssessmentScaleUpdateRequest.RuleDTO();
        ruleDTO.setMinScore(minScore);
        ruleDTO.setMaxScore(maxScore);
        ruleDTO.setResultLevel(resultLevel.trim());
        ruleDTO.setResultSummary(resultSummary.trim());
        ruleDTO.setSuggestion(suggestion == null ? null : suggestion.trim());
        result.add(ruleDTO);
      }
    }

    return result;
  }

  private Integer getIntValue(Cell cell) {
    if (cell == null) {
      return null;
    }
    if (cell.getCellType() == CellType.NUMERIC) {
      return (int) cell.getNumericCellValue();
    }
    String text = getStringValue(cell);
    if (text == null || text.isBlank()) {
      return null;
    }
    return Integer.parseInt(text.trim());
  }

  private String getStringValue(Cell cell) {
    if (cell == null) {
      return null;
    }
    if (cell.getCellType() == CellType.STRING) {
      return cell.getStringCellValue();
    }
    if (cell.getCellType() == CellType.NUMERIC) {
      return String.valueOf((int) cell.getNumericCellValue());
    }
    if (cell.getCellType() == CellType.BOOLEAN) {
      return String.valueOf(cell.getBooleanCellValue());
    }
    return cell.toString();
  }
}