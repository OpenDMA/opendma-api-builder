# Manual validation of generated Python API

Validation Steps:
1. Make sure you have the latest Python and pip installed
2. In `../../src.generated/py` run `pip install .`
3. Open a python command prompt
4. On the python command prompt, run each line of `experiments.py` line by line
5. Alternatively, execute `experiments.py` as a whole

To make updates to the API source that get picked up immediately by Python, run `pip install -e .`.