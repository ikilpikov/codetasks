import { useState } from "react";
import { Difficulties } from "../../Services/services";
import { useTasksStore, usePositionStore } from "../../store";
import "./Difficulty.scss";
const Difficulty = () => {
  const difficulties = ["EASY", "MEDIUM", "HARD"];
  const [buttonIsActive, setButtonIsActive] = useState([false, false, false]);
  const { setDifficulties } = useTasksStore();
  const { setPosition } = usePositionStore();
  const buttonHandler = (
    event: React.MouseEvent<HTMLButtonElement, MouseEvent>,
    index: number
  ) => {
    const buttonText = event.currentTarget.textContent!;

    if (event && difficulties.includes(buttonText as Difficulties))
      setDifficulties(buttonText as Difficulties);
    setPosition("0");
    setButtonIsActive((prevStates) => {
      const newStates = [...prevStates];
      newStates[index] = !newStates[index];
      return newStates;
    });
  };

  return (
    <>
      {difficulties.map((difficulty, index) => {
        return (
          <button
            key={index}
            className={buttonIsActive[index] ? "btn active" : "btn"}
            onClick={(event) => buttonHandler(event, index)}
          >
            {difficulty}
          </button>
        );
      })}
    </>
  );
};

export default Difficulty;
