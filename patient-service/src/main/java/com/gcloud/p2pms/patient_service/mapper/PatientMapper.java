package com.gcloud.p2pms.patient_service.mapper;

import com.gcloud.p2pms.patient_service.dto.PatientRequestDto;
import com.gcloud.p2pms.patient_service.dto.PatientResponseDto;
import com.gcloud.p2pms.patient_service.model.Patient;

import java.time.LocalDate;

public class PatientMapper {
    public static PatientResponseDto toDTO(Patient patient) {
        PatientResponseDto patientDto = new PatientResponseDto();
        patientDto.setId(patient.getId().toString());
        patientDto.setName(patient.getName());
        patientDto.setAddress(patient.getAddress());
        patientDto.setEmail(patient.getEmail());
        patientDto.setDateOfBirth(patient.getDateOfBirth().toString());

        return patientDto;
    }

    public static Patient toModel(PatientRequestDto patientRequestDto) {
        Patient newPatient = new Patient();
        newPatient.setName(patientRequestDto.getName());
        newPatient.setEmail(patientRequestDto.getEmail());
        newPatient.setAddress(patientRequestDto.getAddress());
        newPatient.setDateOfBirth(LocalDate.parse(patientRequestDto.getDateOfBirth()));
        newPatient.setRegisteredDate(LocalDate.parse(patientRequestDto.getRegisteredDate()));

        return newPatient;
    }
}
