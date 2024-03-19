import { create } from "zustand";
import { Difficulties, ITasks, ITestCase } from "./Services/services";

interface ITasksStore extends ITasks {
  difficulties: Difficulties[];
  taskCount: number;
  setDifficulties: (difficulty: Difficulties) => void;
  setTopics: (topic: string[]) => void;
  setLanguages: (language: string[]) => void;
  setTaskCount: (count: number) => void;
  setSize: () => void;
  reset: () => void;
}

export const useTasksStore = create<ITasksStore>((set, get) => ({
  page: 0,
  size: 10,
  difficulties: [],
  topics: [],
  languages: [],
  taskCount: 0,
  setTaskCount: (count: number) => {
    set(() => ({ taskCount: count }));
  },
  setSize: () => {
    const difference = get().taskCount - get().size;
    console.log("Difference", difference);

    if (difference < 10) {
      set((state) => ({ size: state.size + difference }));
    } else {
      set((state) => ({ size: state.size + 10 }));
    }
  },
  setDifficulties: (difficulty: Difficulties) => {
    const currentDifficulties = get().difficulties;
    const indexElem = currentDifficulties?.indexOf(difficulty);

    if (indexElem === -1 || indexElem === undefined)
      set((state) => ({
        difficulties: [...(state.difficulties ?? []), difficulty],
      }));
    else if (currentDifficulties) {
      const newDifficulties = [
        ...currentDifficulties.slice(0, indexElem),
        ...currentDifficulties.slice(indexElem + 1),
      ];
      set({ difficulties: newDifficulties });
    }
  },
  setTopics: (topic: string[]) => {
    set(() => ({
      topics: topic,
    }));
  },
  setLanguages: (language: string[]) => {
    set(() => ({
      languages: language,
    }));
  },
  reset: () => {
    set(() => ({
      difficulties: [],
      topics: [],
      languages: [],
    }));
  },
}));

interface ISandbox {
  selectedLanguage: string;
  condition: string;
  setCondition: (condition: string) => void;
  setSelectedLanguage: (language: string) => void;
}

export const useSandBoxStore = create<ISandbox>((set) => ({
  selectedLanguage: "",
  condition: "",
  setCondition: (condition: string) => {
    set(() => ({ condition: condition }));
  },
  setSelectedLanguage: (language: string) => {
    set(() => ({ selectedLanguage: language }));
  },
}));

interface IConsole {
  consoleText: string;
  timeExecution: string;
  isError: boolean;
  setConsoleText: (text: string) => void;
  setTimeExecution: (time: string) => void;
  setIsError: (isError: boolean) => void;
}
export const useConsoleStore = create<IConsole>((set) => ({
  consoleText: "",
  timeExecution: "",
  isError: false,
  setConsoleText: (text: string) => {
    set(() => ({ consoleText: text }));
  },
  setTimeExecution: (time: string) => {
    set(() => ({
      timeExecution: time,
    }));
  },
  setIsError: (isError: boolean) => {
    set(() => ({
      isError: isError,
    }));
  },
}));

interface ICodeText {
  text: string;
  setText: (text: string) => void;
}

export const useCodeTextStore = create<ICodeText>((set) => ({
  text: "",
  setText: (text: string) => {
    set(() => ({ text: text }));
  },
}));

interface ISolutionInfo {
  result: string;
  executeTime: string;
  visible: boolean;
  setExecuteTime: (time: string) => void;
  setResult: (result: string) => void;
  setVisible: () => void;
}

export const useSolutionStore = create<ISolutionInfo>((set) => ({
  result: "",
  executeTime: "",
  visible: false,
  setExecuteTime: (time: string) => set(() => ({ executeTime: time })),
  setResult: (result: string) => set(() => ({ result: result })),
  setVisible: () => set(({ visible }) => ({ visible: !visible })),
}));

interface IDeleteError {
  isError: boolean;
  setError: (value: boolean) => void;
}

export const useErrorPropStore = create<IDeleteError>((set) => ({
  isError: false,
  setError: (value: boolean) => {
    set(() => ({ isError: value }));
  },
}));

interface ITestCases {
  testcases: ITestCase[];
  addTestCases: () => void;
  changeTestCases: (
    id: number,
    inputValue: string,
    outPutValue: string
  ) => void;
  deleteTestCases: (id: number) => void;
}

export const useTestCaseStore = create<ITestCases>((set, get) => ({
  testcases: [{ inputData: "", outputData: "" }],
  addTestCases: () => {
    const testcase = {
      inputData: "",
      outputData: "",
    };
    set((state) => ({ testcases: [...state.testcases, testcase] }));
  },
  changeTestCases: (id: number, inputValue: string, outputValue: string) => {
    set((state) => ({
      testcases: state.testcases.map((testcase, index) => {
        if (index === id) {
          return {
            ...testcase,
            inputData: inputValue,
            outputData: outputValue,
          };
        }
        return testcase;
      }),
    }));
  },
  deleteTestCases: (id: number) => {
    console.log(id);

    if (get().testcases.length > 1) {
      set((state) => ({
        testcases: state.testcases.filter((_, index) => index !== id),
      }));
    }
  },
}));

interface IPosition {
  position: string;
  setPosition: (position: string) => void;
}
export const usePositionStore = create<IPosition>((set) => ({
  position: "",
  setPosition: (position: string) => {
    set(() => ({ position: position }));
  },
}));

export interface ITaskFull {
  name: string;
  condition: string;
  topic: string;
  languages: string[];
  difficulty: string;
  testcases: ITestCase[];
}
interface ITaskInputs {
  taskInputs: ITaskFull;
  setInputs: (data: ITaskFull) => void;
}

export const useTaskInputsStore = create<ITaskInputs>((set) => ({
  taskInputs: {
    name: "",
    condition: "",
    topic: "",
    languages: [""],
    difficulty: "",
    testcases: [{ inputData: "", outputData: "" }],
  },
  setInputs: (data: ITaskFull) => {
    set(() => ({ taskInputs: data }));
  },
}));

interface ITaskId {
  taskId: number;
  setTaskId: (taskId: number) => void;
}
export const useTaskIdStore = create<ITaskId>((set) => ({
  taskId: 0,
  setTaskId: (taskId: number) => {
    set(() => ({ taskId: taskId }));
  },
}));
