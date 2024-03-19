import React, { FC, useState } from "react";
import { useNavigate } from "react-router-dom";
import { ITask } from "../../Services/services";
import { useSandBoxStore } from "../../store";
import "./Task.scss";

interface ITaskProps extends ITask {
  isClickable: boolean;
}

const Task: FC<ITaskProps> = React.memo(
  ({ id, name, topic, difficulty, languages, isClickable }) => {
    const navigator = useNavigate();
    const [radioLanguage, setRadioLanguage] = useState<string | null>(null);
    const { setSelectedLanguage } = useSandBoxStore();
    const handleClick = (event: React.MouseEvent<HTMLDivElement>) => {
      if (!isClickable) {
        event.stopPropagation();
        return;
      }
      navigator(`/task/${id}`);
    };

    const handleLanguageChange = (language: string) => {
      setSelectedLanguage(language);
      setRadioLanguage(language === radioLanguage ? null : language);
    };
    return (
      <div className="task" onClick={handleClick}>
        <div className="task__header">
          <h2>{name}</h2>
          <span className={`task__difficulty ${difficulty}`}>{difficulty}</span>
        </div>
        <h3>{topic}</h3>
        <span>Languages: </span>
        {isClickable
          ? languages.map((language, index) => (
              <span key={index}>{language + " "}</span>
            ))
          : languages.map((language, index) => (
              <span key={index}>
                <input
                  type="radio"
                  id={`language_${index}`}
                  value={language}
                  checked={language === radioLanguage}
                  onChange={() => handleLanguageChange(language)}
                />
                <label htmlFor={`language_${index}`}>{language}</label>
              </span>
            ))}
      </div>
    );
  }
);

export default Task;
