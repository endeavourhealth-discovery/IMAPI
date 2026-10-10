Feature: IMQ to SQL conversion
  @IMQTest
  Scenario Outline: IMQ converts to SQL without errors
    When IMQ to SQL conversion is executed for <iri>
    Then SQL should be generated successfully

  Examples:
    | iri |
    | "http://endhealth.info/im#Q_RegisteredGMS" |
    | "http://endhealth.info/im#Q_TestQuery" |
    | "http://endhealth.info/qof#36e70531-462a-427a-8ee2-eeb584018cc9" |
    | "http://endhealth.info/qof#da52a278-b035-4df3-986a-747403198172" |
    | "http://endhealth.info/qof#10cff25e-adb1-4b76-9129-bc819ba5aa70" |
    | "http://endhealth.info/qof#b4ab3ade-3814-483c-8f70-153be3db1bc4" |
    | "http://endhealth.info/qof#22edf68c-978d-415d-bb29-c15e7d247ea0" |
    | "http://endhealth.info/qof#fc97829e-7cff-4025-bf9a-139ce99753e9" |
    | "http://endhealth.info/qof#3664cefb-aa73-4866-b8ac-930fd2fc0251" |
    | "http://endhealth.info/qof#12b62913-0b06-45b7-b20b-bc91049d3b65" |
    | "http://endhealth.info/qof#7ec3aa7a-f5af-4fb9-b67b-4b7b61110621" |
    | "http://endhealth.info/qof#89acf3b9-1eda-413b-85f9-dfc8bf3c575f" |
    | "http://endhealth.info/qof#38e66f24-556c-4453-94b8-524608a2e69b" |
    | "http://endhealth.info/qof#739e4c11-559e-4bb9-ae1e-652ed98dab69" |
    | "http://endhealth.info/qof#1adc36e3-980a-44ef-9e0c-9ea33368b7a3" |
    | "http://endhealth.info/qof#a9111729-52ef-4f56-bc64-8cbe019102ef" |
    | "http://endhealth.info/qof#4c6c72be-4171-4a25-b361-a2121ce42be5" |
    | "http://endhealth.info/qof#10033d27-52d5-4a00-a369-da2d5f4369b7" |
    | "http://endhealth.info/qof#e6e5d441-77d5-4520-af34-ec209d62bccb" |
    | "http://endhealth.info/qof#7d57f834-3595-4834-ac29-4a2a33092339" |
    | "http://endhealth.info/qof#7f4e269a-7ac6-4eeb-b39c-0d5a59f6c576" |
    | "http://endhealth.info/qof#3d88a9e9-9f41-48a0-bfa3-3d54d53499cb" |
    | "http://endhealth.info/qof#a4d2d322-041d-4484-9fc2-eddd55a6eaf8" |
    | "http://endhealth.info/qof#5d7112d7-8203-4878-b7e5-2de8bd3a5e93" |
    | "http://endhealth.info/qof#f34d2ecd-3c8b-4367-9936-ec564a1cb9b7" |
    | "http://endhealth.info/qof#b29cc44c-0800-4aa6-b74d-d3128118e19b" |