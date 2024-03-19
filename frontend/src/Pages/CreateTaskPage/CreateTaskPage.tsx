import { useEffect, useState } from "react";
import Select from "react-select";
import { useNavigate } from "react-router-dom";
import { Difficulties } from "../../Services/services";
import {
  useTaskInputsStore,
  useTaskIdStore,
  useTestCaseStore,
} from "../../store";
import { useLanguages } from "../../Hooks/useLanguages";
import { useTopics } from "../../Hooks/useTopics";
import useAddTask from "../../Hooks/useAddTask";
import useUpdateTask from "../../Hooks/useUpdateTask";
import { IOption } from "../../Components/CustomSelect/CustomSelect";
import InOutData from "../../Components/InOutData/InOutData";
import plus from "../../img/plus.svg";
import "./CreateTask.scss";

const CreateTaskPage = () => {
  const navigator = useNavigate();
  const { taskId } = useTaskIdStore();
  const { data: dataLanguages } = useLanguages();
  const { data: dataTopics } = useTopics();
  const { testcases, addTestCases, changeTestCases, deleteTestCases } =
    useTestCaseStore();

  const { mutate } = useAddTask();
  const { mutate: update } = useUpdateTask();
  const [uniqValue, setUniqValue] = useState(0);
  const [topicIndex, setTopicIndex] = useState(-1);
  const [languagesDefault, setLanguagesDefault] = useState<IOption[] | null>(
    []
  );
  const [name, setName] = useState("");
  const [condition, setCondition] = useState("");
  const [topicValue, setTopicValue] = useState("");
  const [languageValue, setLanguageValue] = useState([""]);
  const [buttonIsActive, setButtonIsActive] = useState(0);
  const difficulties = ["EASY", "MEDIUM", "HARD"];
  const { taskInputs } = useTaskInputsStore();
  const optionLanguage = dataLanguages?.data.map((element: IOption) => ({
    value: element.id,
    label: element.name,
  }));
  const optionTopic = dataTopics?.data.map((element: IOption) => ({
    value: element.id,
    label: element.name,
  }));
  useEffect(() => {
    if (taskInputs.name !== "") {
      console.log(taskInputs);

      setName(taskInputs.name);
      setCondition(taskInputs.condition);
      if (taskInputs.difficulty === "MEDIUM") setButtonIsActive(1);
      else if (taskInputs.difficulty === "HARD") setButtonIsActive(2);

      for (let item = 0; item < testcases.length + 1; item++) {
        deleteTestCases(item);
      }

      const topicIndex = optionTopic.findIndex(
        (topic: any) => topic.label === taskInputs.topic
      );
      const languageIndexes = taskInputs.languages.flatMap((language) => {
        return optionLanguage.filter(
          (option: any) => option.label === language
        );
      });
      setTopicValue(taskInputs.topic);
      setLanguageValue(taskInputs.languages);
      setTopicIndex(topicIndex);
      setLanguagesDefault(languageIndexes);
      setUniqValue(Date.now());

      taskInputs.testcases.forEach((item, index) => {
        changeTestCases(index, item.inputData, item.outputData);
        if (taskInputs.testcases.length - 1 !== index) addTestCases();
      });
    }
  }, [taskInputs]);

  const selectOnChange = (event: any) => {
    console.log(event);
    if (Array.isArray(event)) {
      const selectedLanguages = event.map((element: any) => element.label);
      console.log(selectedLanguages);

      setLanguageValue(selectedLanguages);
    } else {
      console.log(event.label);

      setTopicValue(event.label);
    }
  };

  const createTask = () => {
    const data = {
      name: name,
      condition: condition,
      testcases: testcases,
      topic: topicValue,
      languages: languageValue,
      difficulty: difficulties[buttonIsActive] as Difficulties,
    };
    console.log(data);

    mutate(data);
    navigator("/tasks");
  };

  const updateTask = () => {
    const data = {
      id: taskId,
      name: name,
      condition: condition,
      testcases: testcases,
      topic: topicValue,
      languages: languageValue,
      difficulty: difficulties[buttonIsActive] as Difficulties,
    };
    update(data);
    navigator("/tasks");
  };
  return (
    <div className="createTask">
      <div className="createTask__inputs">
        <h3>Name</h3>
        <input
          placeholder="name"
          value={name}
          required
          onChange={(event) => setName(event.target.value)}
        ></input>
        <h3>Condition</h3>
        <textarea
          placeholder="condition"
          required
          value={condition}
          onChange={(event) => setCondition(event.target.value)}
        ></textarea>
      </div>
      <h3>Difficulty</h3>
      <div className="createTask__buttons">
        {difficulties.map((difficulty, index) => {
          return (
            <button
              type="button"
              key={index}
              className={buttonIsActive === index ? "active" : ""}
              onClick={(event) => {
                event.preventDefault();
                setButtonIsActive(index);
              }}
            >
              {difficulty}
            </button>
          );
        })}
      </div>
      <div className="createTask__selects">
        <div className="createTask__selects-item">
          <h3>Languages</h3>
          <Select
            key={uniqValue}
            options={optionLanguage}
            defaultValue={languagesDefault}
            isMulti
            required
            onChange={(event) => selectOnChange(event)}
          />
        </div>
        <div className="createTask__selects-item">
          <h3>Topics</h3>
          <Select
            key={uniqValue}
            defaultValue={optionTopic && optionTopic[topicIndex]}
            options={optionTopic}
            required
            onChange={(event) => selectOnChange(event)}
          />
        </div>
      </div>
      <div className="createTask__inOutData"></div>
      <div className="createTask__inOutData-add">
        <h4>Add testcase</h4>
        <img src={plus} width="40px" onClick={() => addTestCases()} />
      </div>

      {testcases.map((_, index) => {
        return <InOutData key={index} id={index} />;
      })}
      {taskInputs.name === "" ? (
        <button
          onClick={() => createTask()}
          className="createTask__changeButton"
        >
          Create
        </button>
      ) : (
        <button
          onClick={() => updateTask()}
          className="createTask__changeButton"
        >
          Update
        </button>
      )}
    </div>
  );
};

export default CreateTaskPage;
