package com.apptware.hrms.employee;

import lombok.Getter;

@Getter
public enum Skill {
    // Technology Department
    JAVA("Java"),
    PYTHON("Python"),
    JAVASCRIPT("JavaScript"),
    REACT("React"),
    NODE_JS("Node.js"),
    DEVOPS("DevOps"),
    AWS("AWS"),
    DOCKER("Docker"),
    C_PLUS_PLUS("C++"),
    C_SHARP("C#"),
    SQL("SQL"),
    NO_SQL("NoSQL"),
    RUBY("Ruby"),
    SWIFT("Swift"),
    KOTLIN("Kotlin"),
    DATA_SCIENCE("Data Science"),
    MACHINE_LEARNING("Machine Learning"),
    ARTIFICIAL_INTELLIGENCE("Artificial Intelligence"),
    CYBER_SECURITY("Cyber Security"),
    NETWORKING("Networking"),
    DATABASE_ADMINISTRATION("Database Administration"),

    // Design Department
    PHOTOSHOP("Photoshop"),
    ILLUSTRATOR("Illustrator"),
    INDESIGN("InDesign"),
    SKETCH("Sketch"),
    FIGMA("Figma"),
    UI_UX_DESIGN("UI/UX Design"),
    GRAPHIC_DESIGN("Graphic Design"),
    DIGITAL_ILLUSTRATION("Digital Illustration"),
    ANIMATION("Animation"),
    VIDEO_EDITING("Video Editing"),

    // Marketing Department
    DIGITAL_MARKETING("Digital Marketing"),
    SEO("SEO"),
    SOCIAL_MEDIA_MARKETING("Social Media Marketing"),
    CONTENT_MARKETING("Content Marketing"),
    EMAIL_MARKETING("Email Marketing"),
    MARKETING_ANALYTICS("Marketing Analytics"),
    BRAND_MANAGEMENT("Brand Management"),
    CAMPAIGN_MANAGEMENT("Campaign Management"),
    COPYWRITING("Copywriting"),
    VIDEO_PRODUCTION("Video Production"),

    // Sales Department
    SALES_STRATEGY("Sales Strategy"),
    ACCOUNT_MANAGEMENT("Account Management"),
    LEAD_GENERATION("Lead Generation"),
    NEGOTIATION("Negotiation"),
    CUSTOMER_RELATIONSHIP_MANAGEMENT("Customer Relationship Management"),
    SALES_ANALYTICS("Sales Analytics"),
    PRODUCT_DEMONSTRATION("Product Demonstration"),
    PUBLIC_SPEAKING("Public Speaking"),
    PERSUASIVE_COMMUNICATION("Persuasive Communication"),

    // HR Department
    RECRUITMENT("Recruitment"),
    TALENT_ACQUISITION("Talent Acquisition"),
    EMPLOYEE_ENGAGEMENT("Employee Engagement"),
    PERFORMANCE_MANAGEMENT("Performance Management"),
    TRAINING_AND_DEVELOPMENT("Training and Development"),
    LABOR_LAWS("Labor Laws"),
    COMPENSATION_AND_BENEFITS("Compensation and Benefits"),
    WORKPLACE_SAFETY("Workplace Safety"),
    DIVERSITY_AND_INCLUSION("Diversity and Inclusion"),

    // Accounts Department
    FINANCIAL_ANALYSIS("Financial Analysis"),
    ACCOUNTING_SOFTWARE("Accounting Software"),
    TAXATION("Taxation"),
    AUDITING("Auditing"),
    FINANCIAL_REPORTING("Financial Reporting"),
    BUDGETING("Budgeting"),
    COST_ACCOUNTING("Cost Accounting"),
    FINANCIAL_PLANNING("Financial Planning"),
    BOOKKEEPING("Bookkeeping"),

    // Operations Department
    SUPPLY_CHAIN_MANAGEMENT("Supply Chain Management"),
    PROJECT_MANAGEMENT("Project Management"),
    OPERATIONAL_EFFICIENCY("Operational Efficiency"),
    QUALITY_CONTROL("Quality Control"),
    LOGISTICS_MANAGEMENT("Logistics Management"),
    PROCUREMENT("Procurement"),
    INVENTORY_MANAGEMENT("Inventory Management"),
    BUSINESS_PROCESS_IMPROVEMENT("Business Process Improvement"),
    CHANGE_MANAGEMENT("Change Management"),

    // DELIVERY
    STRATEGIC_THINKING("Strategic Thinking"),
    CUSTOMER_FOCUS("Customer Focus"),
    DATA_DRIVEN_DECISION_MAKING("Data-Driven Decision Making"),
    TECHNICAL_ACUMEN("Technical Acumen"),
    LEADERSHIP("Leadership"),
    COMMUNICATION_SKILLS("Communication Skills"),
    PROBLEM_SOLVING("Problem-Solving"),
    AGILITY_AND_FLEXIBILITY("Agility and Flexibility");

    private final String description;

    Skill(String description) {
        this.description = description;
    }

    public static Skill fromDescription(String description) {
        for (Skill skill : values()) {
            if (skill.description.equalsIgnoreCase(description)) {
                return skill;
            }
        }
        throw new IllegalArgumentException("Invalid skill description: " + description);
    }

}