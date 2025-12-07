package com.gcloud.p2pms.patient_service.service;

import com.gcloud.p2pms.patient_service.dto.PatientRequestDto;
import com.gcloud.p2pms.patient_service.dto.PatientResponseDto;
import com.gcloud.p2pms.patient_service.exception.EmailAlreadyExistsException;
import com.gcloud.p2pms.patient_service.exception.ResourceNotFoundException;
import com.gcloud.p2pms.patient_service.grpc.BillingServiceGrpcClient;
import com.gcloud.p2pms.patient_service.kafka.KafkaProducer;
import com.gcloud.p2pms.patient_service.mapper.PatientMapper;
import com.gcloud.p2pms.patient_service.model.Patient;
import com.gcloud.p2pms.patient_service.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PatientService {
    private final PatientRepository patientRepository;
    private final BillingServiceGrpcClient billingServiceGrpcClient;
    private final KafkaProducer kafkaProducer;

    public PatientService(PatientRepository patientRepository, BillingServiceGrpcClient billingServiceGrpcClient, KafkaProducer kafkaProducer) {
        this.patientRepository = patientRepository;
        this.billingServiceGrpcClient = billingServiceGrpcClient;
        this.kafkaProducer = kafkaProducer;
    }

    public List<PatientResponseDto> getPatients() {
        List<Patient> patients = patientRepository.findAll();
//        List<PatientResponseDto> patientsResponseDTOs = patients.stream().map(patient -> PatientMapper.toDTO(patient)).toList();
//        return patientsResponseDTOs;
        return patients.stream().map(PatientMapper::toDTO).toList();
    }

    public PatientResponseDto createPatient(PatientRequestDto patientRequestDto) {
        if(patientRepository.existsByEmail(patientRequestDto.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists" + patientRequestDto.getEmail());
        }

        Patient newPatient = patientRepository.save(PatientMapper.toModel(patientRequestDto));

        billingServiceGrpcClient.createBillingAccount(newPatient.getId().toString(), newPatient.getName(), newPatient.getEmail());

        kafkaProducer.sendEvent(newPatient);

        return PatientMapper.toDTO(newPatient);
    }

    public PatientResponseDto updatePatient(UUID id, PatientRequestDto patientRequestDto) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID: " + id));

        if(patientRepository.existsByEmailAndIdNot(patientRequestDto.getEmail(), id)) {
            throw new EmailAlreadyExistsException("Email already exists" + patientRequestDto.getEmail());
        }

        patient.setName(patientRequestDto.getName());
        patient.setEmail(patientRequestDto.getEmail());
        patient.setAddress(patientRequestDto.getAddress());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDto.getDateOfBirth()));

        Patient updatePatient = patientRepository.save(patient);

        return PatientMapper.toDTO(updatePatient);
    }

    public void deletePatient(UUID id) {
        patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID: " + id));

        patientRepository.deleteById(id);
    }
}
