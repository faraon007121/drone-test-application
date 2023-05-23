package org.lakirev.example.data;

import org.lakirev.example.model.response.MedicationFullInfo;
import org.lakirev.example.model.response.MedicationShortInfo;
import org.lakirev.example.model.entity.Medication;
import org.lakirev.example.model.request.UploadMedicationRequest;

import java.util.Arrays;
import java.util.List;

public final class MedicationTestData {

    public static UploadMedicationRequest getUploadRequest() {
        return new UploadMedicationRequest(
                "testName1",
                13,
                "testCode1",
                null);
    }

    public static List<MedicationShortInfo> getMedicationShortInfoList() {
        return Arrays.asList(
                new MedicationShortInfo(
                        1L,
                        "testName1",
                        13,
                        "testCode1"),
                new MedicationShortInfo(
                        2L,
                        "testName2",
                        15,
                        "testCode2"));
    }

    public static List<MedicationFullInfo> getMedicationFullInfoList() {
        return Arrays.asList(
                new MedicationFullInfo(
                        1L,
                        "testName1",
                        13,
                        "testCode1",
                        null,
                        null,
                        null),
                new MedicationFullInfo(
                        2L,
                        "testName2",
                        15,
                        "testCode2",
                        null,
                        null,
                        null));
    }

    public static List<Medication> getMedicationList() {
        return Arrays.asList(
                Medication.builder()
                        .id(1L)
                        .name("testName1")
                        .weight(13)
                        .code("testCode1")
                        .build(),
                Medication.builder()
                        .id(2L)
                        .name("testName2")
                        .weight(15)
                        .code("testCode2")
                        .build());
    }

}
