from abc import ABC, abstractmethod


class ContentCollector(ABC):

    @abstractmethod
    def collect(self):
        pass

    def close(self):
        pass
