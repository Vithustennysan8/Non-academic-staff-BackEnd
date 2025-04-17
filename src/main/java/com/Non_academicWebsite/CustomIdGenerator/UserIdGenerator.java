package com.Non_academicWebsite.CustomIdGenerator;

import com.Non_academicWebsite.Entity.Faculty;
import com.Non_academicWebsite.Entity.User;
import com.Non_academicWebsite.Repository.UserRepo;
import com.Non_academicWebsite.Service.DepartmentService;
import com.Non_academicWebsite.Service.FacultyService;
import com.Non_academicWebsite.Service.JobPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class UserIdGenerator {
    // Generate user id based on the faculty, department, and position

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private FacultyService facultyService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private JobPositionService jobPositionService;

    private static final HashMap<String, String> faculties = new HashMap<>();
    private static final HashMap<String, String> departments = new HashMap<>();
    private static final HashMap<String, String> positions = new HashMap<>();

    static {
        faculties.put("Faculty of Agriculture", "AG");
        faculties.put("Faculty of Allied Health Sciences", "AH");
        faculties.put("Faculty of Arts", "AR");
        faculties.put("Faculty of Dental Sciences", "DS");
        faculties.put("Faculty of Engineering", "EN");
        faculties.put("Faculty of Management", "MN");
        faculties.put("Faculty of Medicine", "MD");
        faculties.put("Faculty of Science", "SC");
        faculties.put("Faculty of Veterinary Medicine and Animal Science", "VM");
        faculties.put("Registrar's Office", "RO");
        faculties.put("Administration Office", "AO");
        faculties.put("IT Services", "ITS");
        faculties.put("Library Services", "LS");
        faculties.put("Facilities Management", "FM");
        faculties.put("Security Services", "SS");
        faculties.put("Finance Department", "FD");
        faculties.put("Human Resources Department", "HRD");
        faculties.put("Student Affairs Office", "SAO");

        departments.put("Agricultural Biology", "AB");
        departments.put("Agricultural Economics and Business Management", "AEBM");
        departments.put("Agricultural Engineering", "AE");
        departments.put("Agricultural Extension", "AEX");
        departments.put("Animal Science", "AS");
        departments.put("Crop Science", "CS");
        departments.put("Food Science and Technology", "FST");
        departments.put("Soil Science", "SS");
        departments.put("Medical Laboratory Science", "MLS");
        departments.put("Nursing", "NUR");
        departments.put("Physiotherapy", "PT");
        departments.put("Radiography and Radiotherapy", "RR");
        departments.put("Pharmacy", "PH");
        departments.put("Basic Sciences", "BS");
        departments.put("Arabic and Islamic Civilization", "AIC");
        departments.put("Archaeology", "AR");
        departments.put("Classical Languages", "CL");
        departments.put("Economics and Statistics", "ES");
        departments.put("Education", "ED");
        departments.put("English", "ENG");
        departments.put("English Language Teaching", "ELT");
        departments.put("Fine Arts", "FA");
        departments.put("Geography", "GEO");
        departments.put("History", "HIS");
        departments.put("Information Technology", "IT");
        departments.put("Law", "LAW");
        departments.put("Philosophy", "PHI");
        departments.put("Psychology", "PSY");
        departments.put("Political Science", "POL");
        departments.put("Pali and Buddhist Studies", "PBS");
        departments.put("Sinhala", "SIN");
        departments.put("Sociology", "SOC");
        departments.put("Tamil", "TAM");
        departments.put("Community Dental Health", "CDH");
        departments.put("Comprehensive Oral Health Care", "COHC");
        departments.put("Oral Medicine and Periodontology", "OMP");
        departments.put("Oral Pathology", "OP");
        departments.put("Prosthetic Dentistry", "PD");
        departments.put("Restorative Dentistry", "RD");
        departments.put("Oral and Maxillofacial Surgery", "OMS");
        departments.put("Chemical and Process Engineering", "CPE");
        departments.put("Computer Engineering", "CO");
        departments.put("Civil Engineering", "CE");
        departments.put("Electrical and Electronic Engineering", "EEE");
        departments.put("Engineering Mathematics", "EMATH");
        departments.put("Mechanical Engineering", "ME");
        departments.put("Manufacturing and Industrial Engineering", "MIE");
        departments.put("Business Finance", "BF");
        departments.put("Human Resource Management", "HRM");
        departments.put("Management Studies", "MS");
        departments.put("Marketing Management", "MM");
        departments.put("Operations Management", "OM");
        departments.put("Anatomy", "ANAT");
        departments.put("Anaesthesiology and Critical Care", "ACC");
        departments.put("Biochemistry", "BC");
        departments.put("Community Medicine", "CM");
        departments.put("Family Medicine", "FM");
        departments.put("Forensic Medicine", "FMED");
        departments.put("Medical Education", "ME");
        departments.put("Medicine", "MED");
        departments.put("Microbiology", "MB");
        departments.put("Obstetrics and Gynecology", "OBGY");
        departments.put("Paediatrics", "PED");
        departments.put("Parasitology", "PAR");
        departments.put("Pathology", "PATH");
        departments.put("Pharmacology", "PHAR");
        departments.put("Physiology", "PHY");
        departments.put("Psychiatry", "PSY");
        departments.put("Radiology", "RAD");
        departments.put("Surgery", "SUR");
        departments.put("Botany", "BOT");
        departments.put("Chemistry", "CHEM");
        departments.put("Environmental and Industrial Sciences", "EIS");
        departments.put("Geology", "GEO");
        departments.put("Mathematics", "MATH");
        departments.put("Molecular Biology and Biotechnology", "MBB");
        departments.put("Physics", "PHY");
        departments.put("Statistics and Computer Science", "STATCS");
        departments.put("Zoology", "ZOO");
        departments.put("Basic Veterinary Sciences", "BVS");
        departments.put("Farm Animal Production and Health", "FAPH");
        departments.put("Veterinary Clinical Sciences", "VCS");
        departments.put("Veterinary Pathobiology", "VPB");
        departments.put("Veterinary Public Health and Pharmacology", "VPHP");
        departments.put("Administrative Section", "AS");
        departments.put("Technical Section", "TS");
        departments.put("Library Section", "LS");
        departments.put("Maintenance Section", "MS");
        departments.put("Security Section", "SS");
        departments.put("Finance Section", "FS");
        departments.put("HR Section", "HS");
        departments.put("Student Affairs Section", "SAS");
        departments.put("Dean's Office", "DO");

        positions.put("Dean", "DE");
        positions.put("Chief Medical Officer", "CM");
        positions.put("Non Academic Establishment Division", "NA");
        positions.put("Registrar", "RE");
        positions.put("Head of the Department", "HD");
        positions.put("Technical Officer", "TO");
        positions.put("Management Assistant", "MA");
        positions.put("Book Keeper", "BK");
        positions.put("Typist", "TY");
        positions.put("Office Machine Operator", "MO");
        positions.put("Lab Attendant", "LA");
        positions.put("Labourer", "LB");
        positions.put("Driver", "DR");
        positions.put("Carpenter", "CP");

    }

    public String generateCustomUserID(Integer facId, String department, String position) {
        Faculty fac = facultyService.getFac(facId);
        String facultyCode = fac.getAlias();

        String departmentCode = departmentService.getDepartmentCode(fac.getId(), department);
        String positionCode = jobPositionService.getPositionCode(position);

        String prefix = facultyCode + "_" + departmentCode + "_" + positionCode + "_";
        String lastId = userRepo.findTopByIdStartingWithOrderByIdDesc(prefix)
                .map(User::getId).orElse(prefix + "000");

        int lastIdNumber = Integer.parseInt(lastId.substring(prefix.length())) + 1;

        return prefix + String.format("%03d", lastIdNumber);
    }


    public String getFacultyCode(String faculty) {
        return faculties.get(faculty);
    }

    public String getDepartmentCode(String department) {
        return departments.get(department);
    }

    public String getPositionCode(String position) {
        return positions.get(position);
    }
}
