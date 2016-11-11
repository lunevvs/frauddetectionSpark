import re
import sys
import SentenceDetection as sd
import config

if __name__ == "__main__":
        detector = sd.SentenceDetector()
        regex = re.compile(r"[,.\"?!-]")
        textdata = []
        last = len(sys.argv)-1
        for i, line in enumerate(sys.argv):
            if i < last:
                textdata.append(regex.sub('',line))
        res = detector.detect(textdata, regex.sub('',sys.argv[last]), verbose = 'none')
        print("true" if res else "false")
