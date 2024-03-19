import React, { FC } from "react";
import { useTestCaseStore } from "../../store";
import cancel from "../../img/cancel.svg";
import "./InOutData.scss";

interface ITestCaseProps {
  id: number;
}

const InOutData: FC<ITestCaseProps> = ({ id }) => {
  const { deleteTestCases, changeTestCases, testcases } = useTestCaseStore();
  const { inputData, outputData } = testcases[id];
  const setInputValues = (
    event: React.ChangeEvent<HTMLInputElement>,
    inputType: "input" | "output"
  ) => {
    if (inputType === "input")
      changeTestCases(id, event.target.value, outputData);
    else if (inputType === "output")
      changeTestCases(id, inputData, event.target.value);
  };
  return (
    <div className="inoutData">
      <input
        placeholder="inputData"
        value={inputData}
        onChange={(event) => setInputValues(event, "input")}
        required
      />
      <input
        placeholder="outputData"
        value={outputData}
        onChange={(event) => setInputValues(event, "output")}
        required
      />
      <img
        src={cancel}
        className="inoutData__img"
        onClick={() => deleteTestCases(id)}
      />
    </div>
  );
};

export default InOutData;
